package com.autoask.common.util.weixin;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * 类名: SignUtil </br>
 * 描述: 检验signature 工具类 </br>
 * @author ck
 *
 * @Create 2017年5月31日下午2:01:13
 */
public class SignUtil {

	/**
	 *  微信签名方法
	 * @param signature 加密
	 * @param timestamp时间戳
	 * @param nonce 随机数
	 * @return
	 */
	public static boolean checkSignature(String signature, String timestamp, String nonce) {
		// 1.将token、timestamp、nonce三个参数进行字典序排序
		String[] arr = new String[] {WeixinUtil.TOKEN, timestamp, nonce };
		Arrays.sort(arr);

		// 2. 将三个参数字符串拼接成一个字符串进行sha1加密
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			content.append(arr[i]);
		}
		MessageDigest md = null;
		String tmpStr = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
			// 将三个参数字符串拼接成一个字符串进行sha1加密
			byte[] digest = md.digest(content.toString().getBytes());
			tmpStr = byteToStr(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		content = null;
		// 3.将sha1加密后的字符串可与signature对比，标识该请求来源于微信
		return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
	}

	/**
	 * 方法名：byteToStr</br> 详述：将字节数组转换为十六进制字符串</br> 开发人员：souvc </br>
	 *
	 * @param byteArray
	 * @return
	 * @throws
	 */
	private static String byteToStr(byte[] byteArray) {
		String strDigest = "";
		for (int i = 0; i < byteArray.length; i++) {
			strDigest += byteToHexStr(byteArray[i]);
		}
		return strDigest;
	}

	/**
	 * 方法名：byteToHexStr</br> 详述：将字节转换为十六进制字符串</br> 开发人员：souvc</br>
	 *
	 * @param mByte
	 * @return
	 * @throws
	 */
	private static String byteToHexStr(byte mByte) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] tempArr = new char[2];
		tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
		tempArr[1] = Digit[mByte & 0X0F];
		String s = new String(tempArr);
		return s;
	}
}