package com.autoask.service.impl.common;

import com.autoask.common.exception.ApiException;
import com.autoask.common.util.CodeGenerator;
import com.autoask.common.util.Constants;
import com.autoask.common.util.FileUtil;
import com.autoask.common.util.PropertiesUtil;
import com.autoask.service.common.UploadService;
import com.autoask.service.file.FileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author hyy
 * @create 2016-08-04 19:32
 */
@Service("uploadService")
public class UploadServiceImpl implements UploadService {

    @Autowired
    private LocalFileManager localFileManager;

    @Autowired
    private FileService fileService;

    @Override
    public String uploadLocalCommon(MultipartFile file) throws ApiException {
        String fileName = file.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        byte[] fileBytes = FileUtil.buildFileBytes(file);
        String keyType = CodeGenerator.uuid() + fileType;
        localFileManager.upload(fileBytes, keyType);
        return keyType;
    }

    @Override
    public String uploadQiNiuImg(MultipartFile file) throws ApiException {
        String fileName = file.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        byte[] fileBytes = FileUtil.buildFileBytes(file);
        String key = CodeGenerator.uuid() + fileType;
        Map<String, String> result = fileService.upload(fileBytes, key, true);
        if (!StringUtils.equals(Constants.Result.SUCCESS, result.get("result"))) {
            throw new ApiException("文件上传失败");
        }
        StringBuffer urlBuffer = new StringBuffer();
        urlBuffer.append(PropertiesUtil.getProperty("qiniu.server.url", "")).append("/").
                append(key);
        return urlBuffer.toString();
    }
}
