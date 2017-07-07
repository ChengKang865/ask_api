package com.autoask.common.util;

import java.math.BigDecimal;

import com.autoask.common.exception.ApiException;

/**
 * @author hyy
 * @create 2016-10-27 17:34
 */
public class BigDecimalUtil {

    /**
     * 返回对应的 单位为分的金额
     *
     * @param amount 单位为元
     * @return
     */
    public static int decimal2Int(BigDecimal amount) {
        return amount.multiply(new BigDecimal(100)).intValue();
    }

    public static BigDecimal int2Decimal(int amount) {
        BigDecimal val = new BigDecimal(amount);
        return val.divide(new BigDecimal(100));
    }

    public static BigDecimal clean(BigDecimal num) {
        return null == num ? BigDecimal.ZERO : num;
    }

    /**
     * 计算ali支付手续费
     * 自助签约单笔默认费率 0.5%，最低1元/笔，最高25元/笔
     */
    public static BigDecimal computeAlipayFee(BigDecimal amount) {
        BigDecimal minFee = BigDecimal.valueOf(PropertiesUtil.getProperty("alipay.fee.min", 1));
        BigDecimal maxFee = BigDecimal.valueOf(PropertiesUtil.getProperty("alipay.fee.max", 25));
        BigDecimal rates = BigDecimal.valueOf(PropertiesUtil.getProperty("alipay.fee.rates", 5));
        BigDecimal rateBase = BigDecimal.valueOf(PropertiesUtil.getProperty("alipay.fee.rates.base", 1000));

        BigDecimal fee = amount.multiply(rates).divide(rateBase);
        if (fee.compareTo(minFee) < 0) {
            return minFee;
        }
        if (fee.compareTo(maxFee) > 0) {
            return maxFee;
        }
        return fee;
    }

    public static BigDecimal computeRealAmount(BigDecimal amount) {
        BigDecimal minFee = BigDecimal.valueOf(PropertiesUtil.getProperty("alipay.fee.min", 1));
        BigDecimal maxFee = BigDecimal.valueOf(PropertiesUtil.getProperty("alipay.fee.max", 25));
        BigDecimal rates = BigDecimal.valueOf(PropertiesUtil.getProperty("alipay.fee.rates", 5));
        BigDecimal rateBase = BigDecimal.valueOf(PropertiesUtil.getProperty("alipay.fee.rates.base", 1000));

        BigDecimal maxAmount = maxFee.multiply(rateBase).divide(rates).subtract(maxFee);
        BigDecimal minAmount = minFee.multiply(rateBase).divide(rates).subtract(minFee);

        BigDecimal otherFee;
        if (amount.compareTo(minAmount) <= 0) {
            return amount.add(minFee);
        }
        if (amount.compareTo(maxAmount) >= 0) {
            return amount.add(maxFee);
        }
        BigDecimal otherRate = rateBase.multiply(BigDecimal.ONE).subtract(rates);
        amount = amount.add(amount.multiply(otherRate).divide(rateBase));

//        return amount;
        return amount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    
    /**
     * 提供精确加法计算的add方法
     * @param value1 被加数
     * @param value2 加数
     * @return 两个参数的和
     */
    public static BigDecimal add(BigDecimal value1,BigDecimal value2){
    return new BigDecimal(value1.add(value2).doubleValue());
    }
    /**
     * 提供精确减法运算的sub方法
     * @param value1 被减数
     * @param value2 减数
     * @return 两个参数的差
     */
    public static BigDecimal sub(BigDecimal value1,BigDecimal value2){
    return new BigDecimal(value1.subtract(value2).doubleValue());
    }
    /**
     * 提供精确乘法运算的mul方法
     * @param value1 被乘数
     * @param value2 乘数
     * @return 两个参数的积
     */
    public static BigDecimal mul(BigDecimal value1,BigDecimal value2){
    return new BigDecimal(value1.multiply(value2).doubleValue());
    }
    /**
     * 提供精确的除法运算方法div
     * @param value1 被除数
     * @param value2 除数
     * @param scale 精确范围
     * @return 两个参数的商
     * @throws IllegalAccessException
     * @throws ApiException 
     */
    public static BigDecimal div(BigDecimal value1,BigDecimal value2,int scale) throws  ApiException{
    //如果精确范围小于0，抛出异常信息
	    if(scale<0){ 
	    	throw new ApiException("精确度不能小于0");
	    }
	    Double d=value1.divide(value2, scale).doubleValue();
	return new BigDecimal(d);
    }

    public static void main(String[] args) {
        System.out.println(computeRealAmount(new BigDecimal(2)));
    }
}
