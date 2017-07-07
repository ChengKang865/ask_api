package com.autoask.common.util;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.autoask.common.exception.ApiException;

public class DateUtil {

    /**
     * 年月日时分秒(无下划线) yyyyMMddHHmmss
     */
    public static final String dateLong = "yyyyMMddHHmmss";

    /**
     * 完整时间 yyyy-MM-dd HH:mm:ss
     */
    public static final String simple = "yyyy-MM-dd HH:mm:ss";

    /**
     * 年月日(无下划线) yyyyMMdd
     */
    public static final String dateShort = "yyyyMMdd";


    /**
     * 获取系统当期年月日(精确到天)，格式：yyyyMMdd
     *
     * @return String
     */
    public static String getDateShort() {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat(dateShort);
        return df.format(date);
    }

    /**
     * 获取系统当前日期(精确到毫秒)，格式：yyyy-MM-dd HH:mm:ss
     *
     * @return String
     */
    public static String getDateFormatter() {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat(simple);
        return df.format(date);
    }

    /**
     * 返回系统当前时间(精确到毫秒)
     * PS: 可以作为一个唯一的订单编号
     *
     * @return 以yyyyMMddHHmmss为格式的当前系统时间
     */
    public static String getDataToSecond() {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat(dateLong);
        return df.format(date);
    }

    /**
     * 获取当前时间
     *
     * @return 返回当前时间
     */
    public static Date getDate() {
        return new Date();
    }
   /**
    * 得到时间之差 秒
    * @param date
    * @return
    */
    public static Long timeDifference(Date date){
    	SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化时间 
    	String x = d.format(date);
    	String y = d.format(new Date());// 按以上格式 将当前时间转换成字符串 
    	Long result = 0L;
    	try {
			result = (d.parse(y).getTime() - d.parse(x).getTime()) / 1000;
			// 当前时间减去测试时间  
            // 这个的除以1000得到秒，相应的60000得到分，3600000得到小时  
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
    }

    /**
     * 获取当前时间
     *
     * @return 返回当前时间
     */
    public static Date getDate(Long param) {
        return new Date(param);
    }

    /**
     * 获取当前时间毫秒数
     *
     * @return 返回当前时间毫秒数
     */
    public static Long getTime() {
        return getDate().getTime();
    }


    /**
     * 根据formatString的格式字符串输出日期格式
     *
     * @param dateTime     时间参数
     * @param formatString 格式字符串
     * @return 返回格式化之后的字符串
     */
    public static String format(Long dateTime, String formatString) {
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        Date date = getDate(dateTime);
        return format.format(date);
    }

    /**
     * 在日期Date上进行加、减算法操作
     *
     * @param date  被加减的日期对象
     * @param type  对时间的年、月、日、时、分、秒操作，如Calendar.DAY_OF_MONTH
     * @param param 需要加、或者减的时间数
     * @return 返回计算后的时间的毫秒数
     */
    public static Long addTime(Date date, int type, int param) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(type, param);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取date的对应的type类型的值
     *
     * @param date 日期对象
     * @param type 类型，对时间的年、月、日、时、分、秒操作，如Calendar.HOUR
     * @return 返回取值
     */
    public static int getValue(Date date, int type) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(type);
    }

    /**
     * 根据当前时间为过期时间，计算开始时间
     *
     * @return
     */
    public static Long getExpiredLimitTime() {
        int expiredHours = Integer.valueOf(PropertiesUtil.getProperty("carplay.max.expired.hours", 7 * 24));
        return DateUtil.getTime() - expiredHours * Constants.DAY_MILLISECONDS / 24;
    }

    /**
     * 获取当前日期的零点
     *
     * @param date
     * @return
     */
    public static Long getZeroTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取当前日期的下一个零点
     *
     * @param date
     * @return
     */
    public static Long getNextZeroTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static Date getEarlyYearTime(Date date) {
        Calendar ins = Calendar.getInstance();
        ins.setTime(date);
        ins.set(Calendar.MONTH, 0);
        ins.set(Calendar.DAY_OF_MONTH, 1);
        ins.set(Calendar.HOUR, 0);
        ins.set(Calendar.MINUTE, 0);
        ins.set(Calendar.SECOND, 0);
        ins.set(Calendar.MILLISECOND, 0);
        return ins.getTime();
    }

    public static Date getEarlyMonthTime(Date date) {
        Calendar ins = Calendar.getInstance();
        ins.setTime(date);
        ins.add(Calendar.MONTH, 0);
        ins.set(Calendar.DAY_OF_MONTH, 1);
        ins.set(Calendar.HOUR, 0);
        ins.set(Calendar.MINUTE, 0);
        ins.set(Calendar.SECOND, 0);
        ins.set(Calendar.MILLISECOND, 0);
        return ins.getTime();
    }

    public static Date getEarlyDayTime(Date date) {
        Calendar ins = Calendar.getInstance();
        ins.setTime(DateUtil.getDate());
        ins.set(Calendar.HOUR, 0);
        ins.set(Calendar.MINUTE, 0);
        ins.set(Calendar.SECOND, 0);
        ins.set(Calendar.MILLISECOND, 0);
        return ins.getTime();
    }
    
    public static Long datToSecond(int day) throws ApiException{
    	if(day <= 0){
    		throw new ApiException("失效时间不能小于1");
    	}
    	Long expireSecondSsecone = new Long((day * 24 * 60 * 60));
		return expireSecondSsecone;
    }
}
