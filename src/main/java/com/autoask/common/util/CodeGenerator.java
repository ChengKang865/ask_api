package com.autoask.common.util;

import com.autoask.common.exception.ApiException;
import com.ibm.icu.text.SimpleDateFormat;

import java.math.BigInteger;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * 生成随机验证码和主键信息
 *
 * @author hyy
 */
public class CodeGenerator {

    /**
     * 生成数据表的主键，采用UUID生成
     *
     * @return 主键字符串
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成手机验证码
     *
     * @return 返回四位验证码
     */
    public static String generatorVerifyCode() {
        int code = (int) Math.floor(Math.random() * 10000);
        String codeStr = "0000" + code;
        return codeStr.substring(codeStr.length() - 4);
    }

    /**
     * 生成随机数 数字以及字母的组合
     *
     * @param length
     * @return
     */
    public static String getStringRandom(int length) {

        String val = "";
        Random random = new Random();

        //参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    /**
     * 生成验证码
     *
     * @return
     */
    public static String generateValidateCode() {
        return getStringRandom(6).toLowerCase();
    }


    public static String generateCardId() throws ApiException {
        Date now = DateUtil.getDate();
        String timeStr = coverTo36(now.getTime());
        String randomStr = getStringRandom(5);
        return timeStr + randomStr.toLowerCase();
    }

    /**
     * 生成订单id
     *
     * @return
     */
    public static String generatorOrderId() throws ApiException {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+ generatorVerifyCode();
    }

    /**
     * 数字转换成数字字母组合的36进制
     *
     * @param num
     * @return
     */
    public static String coverTo36(long num) throws ApiException {
        char[] source = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        StringBuilder resultBuilder = new StringBuilder();
        long tmpNum = num;
        while (tmpNum != 0) {
            long tmpIndex = tmpNum % 36L;
            tmpNum = tmpNum / 36L;
            if (tmpIndex >= 36L) {
                throw new ApiException("系统繁忙请重试");
            }
            resultBuilder.append(source[(int) tmpIndex]);
        }
        return resultBuilder.reverse().toString();
    }
}
