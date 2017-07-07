package com.autoask.service.common;

import com.autoask.common.exception.ApiException;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author hyy
 * @create 2016-08-04 19:31
 */
public interface UploadService {

    /**
     * 上传文件到本地服务器
     * 返回文件的key
     *
     * @param file
     * @return
     * @throws ApiException
     */
    String uploadLocalCommon(MultipartFile file) throws ApiException;

    String uploadQiNiuImg(MultipartFile file) throws ApiException;
}
