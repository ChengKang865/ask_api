package com.autoask.freemarker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


public class PathFreeMarkerView extends FreeMarkerView {

    private static final Logger LOG = LoggerFactory.getLogger(PathFreeMarkerView.class);

    private static final String CONTEXT_PATH = "basePath";

    @Override
    protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) throws Exception {
        StringBuffer urlBuffer = new StringBuffer();
        //TODO 获取服务器绝对路径
        urlBuffer.append(request.getScheme()).append("://").
                append(request.getServerName()).append(":").append(request.getServerPort());
        LOG.debug("request context path is {}",urlBuffer.toString());

//        model.put(CONTEXT_PATH, urlBuffer.toString());
        model.put(CONTEXT_PATH, request.getContextPath());

        super.exposeHelpers(model, request);
    }
}
