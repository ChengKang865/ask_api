package com.autoask.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.lang3.StringUtils;

import com.autoask.common.exception.ApiException;

/**
 * 根据图片地址下载二维码
 * 
 * @author ck
 *
 * @Create 2017年6月12日下午4:35:02
 */
public class ImageUtil {
	public static void downloadPhotos(String imgName, String urlString) throws Exception {
		try {
			if (StringUtils.isEmpty(imgName)) {
				throw new ApiException("名称不能为空");
			}
			if (StringUtils.isEmpty(urlString)) {
				throw new ApiException("图片地址不能为空");
			}
			// 构造URL
			URL url = new URL(urlString);
			// 打开连接
			URLConnection con = url.openConnection();
			// 输入流
			InputStream is = con.getInputStream();
			// 1K的数据缓冲
			byte[] bs = new byte[1024];
			// 读取到的数据长度
			int len;
			// 输出的文件流
			String downloadDir = "D:\\autoAskImage\\weChatQR\\";
			File downloadFilePath = new File(downloadDir);
			if (downloadFilePath.exists() == false) // 如果该目录不存在,则创建之
				downloadFilePath.mkdirs();
			String FILENAME = downloadDir + "\\" + imgName + ".png"; // 生成新的照片名
			OutputStream os = new FileOutputStream(FILENAME); // 开始读取
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
			os.close();
			is.close();
		} catch (Exception e) {
			throw new ApiException(e.fillInStackTrace().getMessage());
		}
	}
}
