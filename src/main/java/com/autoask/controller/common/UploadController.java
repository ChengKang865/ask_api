package com.autoask.controller.common;

import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.DateUtil;
import com.autoask.service.common.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * @author hyy
 * @create 2016-08-04 19:29
 */
@RequestMapping(value = "/upload")
@Controller
public class UploadController {

    private static final Logger LOG = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    private UploadService uploadService;

    /**
     * 上传公共图片
     *
     * @return 图片的uuid
     */
    @RequestMapping(value = "/local/common/", method = RequestMethod.POST, headers = {"Content-Type=multipart/form-data"})
    @ResponseBody
    public ResponseDo localCommon(@RequestBody MultipartFile file) {
        LOG.debug("upload common file size: {}", file.getSize());
        try {
            String fileKey = uploadService.uploadLocalCommon(file);

            return ResponseDo.buildSuccess(fileKey);
        } catch (Exception e) {
            LOG.warn(e.toString());
            e.printStackTrace();
            return ResponseDo.buildError("上传文件失败");
        }
    }

    /**
     * 上传到七牛 返回url
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/qiniu/common/", method = RequestMethod.POST, headers = {"Content-Type=multipart/form-data"})
    @ResponseBody
    public ResponseDo qiNiu(@RequestBody MultipartFile file) {
        LOG.debug("upload common file size:{}", file.getSize());
        try {
            String fileUrl = uploadService.uploadQiNiuImg(file);
            return ResponseDo.buildSuccess(fileUrl);
        } catch (ApiException e) {
            LOG.error("上传文件失败:" + e.getMessage());
            return ResponseDo.buildError("上传文件失败");
        }
    }
}