package com.autoask.controller.weixin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.autoask.common.util.weixin.MessageUtil;
import com.autoask.common.util.weixin.SignUtil;
import com.autoask.controller.common.BaseController;
import com.autoask.service.weiixn.CoreService;

/**
 * 微信
 * 
 * @author ck
 *
 * @Create 2017年5月27日下午1:47:17
 */
@Controller
@RequestMapping(value = "/core/")
public class WeiXinController extends BaseController {

	@Autowired
	private CoreService coreService;

	private static final Logger LOG = LoggerFactory.getLogger(WeiXinController.class);

	/**
	 * get请求，测试接口
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "weiXinCore/", method = RequestMethod.GET)
	@ResponseBody
	public void CoreDoGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		/**
		 * 微信加密签名
		 */
		String signature = request.getParameter("signature");
		LOG.info("signature================！" + signature);
		/**
		 * 时间戳
		 */
		String timestamp = request.getParameter("timestamp");
		/**
		 * 随机数
		 */
		String nonce = request.getParameter("nonce");
		/**
		 * 随机字符串
		 */
		String echostr = request.getParameter("echostr");
		PrintWriter out = response.getWriter();

		/**
		 * 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
		 */
		if (SignUtil.checkSignature(signature, timestamp, nonce)) {
			LOG.info("微信接入成功================！" + echostr);
			out.print(echostr);
		}
		out.close();
		out = null;
	}

	/**
	 * post请求
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "weiXinCore/", method = RequestMethod.POST)
	@ResponseBody
	public void CoreDoPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）    
        request.setCharacterEncoding("UTF-8");    
        response.setCharacterEncoding("UTF-8");    
        String encryptType = request.getParameter("encrypt_type");    
        // 微信加密签名    
        String signature = request.getParameter("signature");    
        // 时间戳    
        String timestamp = request.getParameter("timestamp");   
        // 随机数    
        String nonce = request.getParameter("nonce");    
        // 响应消息    
        try{  
            PrintWriter out = response.getWriter();   
            if (SignUtil.checkSignature(signature, timestamp, nonce)) {    
                Map<String, String> requestMap = null;
                //加密
                  if("aes".equals(encryptType)){  
                    requestMap=MessageUtil.parseXmlCrypt(request);  
                    String respXml=coreService.processRequest(requestMap);   
                    respXml=MessageUtil.getWxCrypt().encryptMsg(respXml,timestamp,nonce);  
                    out.print(respXml);    
                  }else{ 
                	  //明文
                    requestMap=MessageUtil.parseXml(request);  
                    String respXml=coreService.processRequest(requestMap);   
                    out.print(respXml);  
                  }  
                out.close();    
            }    
        }catch(Exception e){  
            e.printStackTrace();  
        }  
	}
}
