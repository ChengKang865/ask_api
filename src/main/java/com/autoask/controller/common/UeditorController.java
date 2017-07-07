package com.autoask.controller.common;

import com.autoask.service.common.UploadService;
import com.autoask.ueditor.ActionEnter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hyy on 2016/12/4.
 */
@RequestMapping(value = "/ueditor")
@Controller
public class UeditorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UeditorController.class);

    @Autowired
    private UploadService uploadService;

    /**
     * get config
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/config", method = RequestMethod.GET)
    public void config(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        String rootPath = request.getSession().getServletContext().getRealPath("/");

        try {
            String exec = new ActionEnter(request, rootPath).exec();
            PrintWriter writer = response.getWriter();
            writer.write(exec);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            LOGGER.error("Obtain config failure:", e);
        }

    }

    /**
     * 多文件上传支持
     *
     * @param upfile
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public Map uploadFile(@RequestParam(value = "upfile", required = false) MultipartFile[] upfile) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        if (upfile != null && upfile.length > 0) {
            //循环获取file数组中得文件
            for (int i = 0; i < upfile.length; i++) {
                MultipartFile file = upfile[i];
                try {
                    String fileUrl = uploadService.uploadQiNiuImg(file);
                    map.put("url", fileUrl);
                    map.put("name", fileUrl);
                    map.put("state", "SUCCESS");
                } catch (Exception e) {
                    LOGGER.error("Upload file to qiniu failure", e);
                    map.put("state", "上传失败!");
                }
            }
        }
        return map;
    }
}
