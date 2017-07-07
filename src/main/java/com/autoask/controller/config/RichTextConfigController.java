package com.autoask.controller.config;

import com.autoask.common.ResponseDo;
import com.autoask.common.exception.ApiException;
import com.autoask.common.util.Constants;
import com.autoask.entity.mongo.config.RichTextConfig;
import com.autoask.service.config.RichTextConfigService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by hyy on 2016/12/4.
 */
@Controller
@RequestMapping("config/richtext/")
public class RichTextConfigController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RichTextConfigController.class);

    @Autowired
    private RichTextConfigService richTextConfigService;


    /**
     * 添加富文本的配置
     *
     * @param config
     * @return
     */
    @RequestMapping(value = "add/{type}/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo add(@PathVariable("type") String type, @RequestBody @Valid RichTextConfig config) {
        try {
            if (!Constants.RichTextType.RichTextSupportTypes.contains(type)) {
                throw new ApiException("类型输入不正确");
            }

            if (StringUtils.isBlank(config.getChannel())) {
                config.setChannel(Constants.BannerChannel.PC);
            }
            config.setType(type);
            richTextConfigService.addConfig(config);
            return ResponseDo.buildSuccess("添加成功");
        } catch (ApiException e) {
            LOGGER.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    /**
     * 修改富文本的配置
     *
     * @param config
     * @return
     */
    @RequestMapping(value = "modify/{type}/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo modify(@PathVariable("type") String type, @RequestBody @Valid RichTextConfig config) {
        try {
            if (!Constants.RichTextType.RichTextSupportTypes.contains(type)) {
                throw new ApiException("类型输入不正确");
            }

            if (StringUtils.isBlank(config.getChannel())) {
                config.setChannel(Constants.BannerChannel.PC);
            }

            config.setType(type);
            richTextConfigService.modifyConfig(config);
            return ResponseDo.buildSuccess("修改成功");
        } catch (ApiException e) {
            LOGGER.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    /**
     * 查詢富文本相關的
     *
     * @return
     */
    @RequestMapping(value = "query/{type}/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo query(@PathVariable("type") String type,
                            @RequestParam(name = "channel", required = false) String channel,
                            @RequestParam(name = "title", required = false) String title) {
        try {
            return ResponseDo.buildSuccess(richTextConfigService.queryConfigs(type, channel, title));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    /**
     * 查詢富文本相關的
     *
     * @return
     */
    @RequestMapping(value = "detail/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseDo queryById(@RequestParam(name = "id", required = true) String id) {
        try {
            return ResponseDo.buildSuccess(richTextConfigService.getConfigById(id));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "delete/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDo delete(@RequestParam(name = "id", required = true) String id) {
        try {
            richTextConfigService.deleteConfig(id);
            return ResponseDo.buildSuccess("删除成功");
        } catch (ApiException e) {
            LOGGER.error(e.getMessage());
            return ResponseDo.buildError(e.getMessage());
        }
    }

}
