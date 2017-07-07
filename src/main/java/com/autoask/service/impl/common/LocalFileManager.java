package com.autoask.service.impl.common;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hyy
 * @create 2016-08-05 15:56
 */
@Service("localFileManager")
public class LocalFileManager {

    private static final Logger LOG = LoggerFactory.getLogger(LocalFileManager.class);

    private static final String LOCAL_PROPERTY = "local.common.filePath";

    /**
     * 判断目录是否存在
     *
     * @param key
     * @return
     */
    public boolean isExist(String key) {
        String filePath = PropertiesUtil.getProperty(LOCAL_PROPERTY, "photos/") + key;
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 创建不存在的目录
     *
     * @param path
     */
    private void makeDirIfNotExists(String path) {
        File pathFile = new File(path);
        if (!pathFile.exists()) {
            boolean succeed = pathFile.mkdirs();
            if (!succeed) {
                LOG.warn("make directories failure, dir:{}", path);
            }
        }
    }


    /**
     * 上传文件 如果文件存在则覆盖
     *
     * @param data
     * @param key
     * @throws ApiException
     */
    public void upload(byte[] data, String key) throws ApiException {
        LOG.debug("Begin save file");

        String filePath = PropertiesUtil.getProperty(LOCAL_PROPERTY, "/photos/") + key;
        FileOutputStream outputStream = null;

        try {
            String dirPath = filePath.substring(0, filePath.lastIndexOf("/"));
            makeDirIfNotExists(dirPath);
            deleteIfExists(key);
            outputStream = new FileOutputStream(filePath);
            outputStream.write(data);
        } catch (FileNotFoundException e) {
            LOG.error(e.getMessage());
            throw new ApiException("保存文件失败", e);
        } catch (IOException e) {
            LOG.error(e.getMessage());
            throw new ApiException("保存文件失败", e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    LOG.warn("Close outputStream failure");
                }
            }
        }
    }


    /**
     * 如果key对应的文件存在删除
     *
     * @param key
     * @throws ApiException
     */
    public void deleteIfExists(String key) throws ApiException {
        String filePath = PropertiesUtil.getProperty(LOCAL_PROPERTY, "/photos") + key;
        File file = new File(filePath);
        if (file.exists()) {
            boolean succeed = file.delete();
            if (!succeed) {
                LOG.warn("delete file failure file key:{}", key);
            }
        }
    }
}
