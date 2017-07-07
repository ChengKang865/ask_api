package com.autoask.service.impl.file;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.Constants;
import com.autoask.qiniu.AuthManager;
import com.autoask.service.file.FileService;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hyy
 * @create 2016-09-03 19:37
 */
@Service
@Qualifier("fileService")
public class QiNiuFileServiceImpl implements FileService {

    private static final Logger LOG = LoggerFactory.getLogger(QiNiuFileServiceImpl.class);

    //    private UploadManager uploadManager = new UploadManager();

    private static UploadManager uploadManager;

    static {
        uploadManager = new UploadManager();
    }

    @Override
    public Map<String, String> upload(byte[] data, String key, boolean override) throws ApiException {
        Map<String, String> result = new HashMap<String, String>(0);

        try {
            String upToken = "";
            if (override) {
                upToken = AuthManager.getInstance().getOverrideUpToken(key);
            } else {
                upToken = AuthManager.getInstance().getUpToken();
            }

            Response response = uploadManager.put(data, key, upToken);
            LOG.info(response.bodyString());
            result = response.jsonToObject(HashMap.class);
            if (response.isOK()) {
                result.put("result", Constants.Result.SUCCESS);
            } else {
                result.put("result", Constants.Result.FAILURE);
            }
        } catch (QiniuException e) {
            Response res = e.response;
            // 请求失败时简单状态信息
            LOG.warn(res.toString());
            LOG.warn(e.getMessage(), e);
            try {
                // 响应的文本信息
                LOG.warn(res.bodyString());
            } catch (QiniuException e1) {
                LOG.warn(e.getMessage(), e);
            }
            throw new ApiException("未能成功上传至服务器");
        }
        return result;
    }

    @Override
    public void delete(String key) throws ApiException {
        AuthManager instance = AuthManager.getInstance();
        try {
            instance.getBucketManager().delete(instance.getBucket(), key);
        } catch (QiniuException e) {
            LOG.warn(e.getMessage(), e);
            throw new ApiException("删除文件失败");
        }
    }

    @Override
    public boolean isExist(String key) {
        AuthManager instance = AuthManager.getInstance();
        FileInfo fileInfo = null;
        try {
            fileInfo = instance.getBucketManager().stat(instance.getBucket(), key);
        } catch (QiniuException e) {
            LOG.warn(e.getMessage(), e);
            // throw new ApiException("查找文件失败");
        }
        return fileInfo != null;
    }

    @Override
    public void move(String key, String targetKey) throws ApiException {
        AuthManager instance = AuthManager.getInstance();
        try {
            instance.getBucketManager().move(instance.getBucket(), key, instance.getBucket(), targetKey);
        } catch (QiniuException e) {
            LOG.warn(e.getMessage(), e);
            throw new ApiException("移动文件失败");
        }
    }

    @Override
    public void rename(String oldName, String newName) throws ApiException {
        AuthManager instance = AuthManager.getInstance();
        try {
            instance.getBucketManager().rename(instance.getBucket(), oldName, newName);
        } catch (QiniuException e) {
            LOG.warn(e.getMessage(), e);
            throw new ApiException("移动重命名失败");
        }
    }
}
