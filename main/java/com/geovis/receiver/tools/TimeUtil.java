package com.geovis.receiver.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <br>
 *
 * @Title: TimeUtil.java
 * @Package org.cimiss2.dwp.RADAR.tool
 * @Description: TODO(时间工具类)
 *
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2017年12月15日 下午7:27:46   wuzuoqiang    Initial creation.
 * </pre>
 */
public class TimeUtil {

    /**
     * 缺省的日期显示格式： yyyy-MM-dd
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 缺省的日期时间显示格式：yyyy-MM-dd HH:mm:ss
     */
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_FMT_YMD = "yyyyMMdd";
    public static final String DATE_FMT_YMDH = "yyyyMMddHH";
    public static final String DATE_FMT_YMDHM = "yyyyMMddHHmm";
    public static final String DATE_FMT_YMDHMS = "yyyyMMddHHmmss";

    /**
     * @throws
     * @Title: getSysTime
     * @Description: TODO(获取当前系统时间)
     * @param: @return
     * @return: String
     */
    public static String getSysTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String currentSysTime = dateFormat.format(date);
        return currentSysTime;
    }

    /**
     * 功能：获取系统时间
     *
     * @param strDateFormat 时间类格式：yyyy-MM-dd HH:mm:ss.SSS / yyyyMMdd /yyyyMMdd HH:mm:ss
     * @return ：返回指定格式的系统时间字符串
     */
    public static String getSysTime(String strDateFormat) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        Date date = new Date();
        String currentSysTime = dateFormat.format(date);
        return currentSysTime;
    }

    /**
     * 获取制定格式的日期字符串
     * 时间类格式：yyyy-MM-dd HH:mm:ss.SSS / yyyyMMdd /yyyyMMdd HH:mm:ss
     *
     * @param date          时间
     * @param strDateFormat 时间类格式：yyyy-MM-dd HH:mm:ss.SSS / yyyyMMdd /yyyyMMdd HH:mm:ss
     * @return
     */
    public static String date2String(Date date, String strDateFormat) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String strTime = dateFormat.format(date);
        return strTime;
    }

    /**
     * @param dateTime 需要格式化的时间
     * @param from     原始的格式
     * @param to       转换后的格式
     * @return
     */
    public static String dateTimeStr2Str(String dateTime, String from, String to) {
        Date date = String2Date(dateTime, from);
        String result = date2String(date, to);

        return result;
    }

    /**
     * @param dateTime 需要格式化的时间
     * @param from     原始的格式
     * @param to       转换后的格式
     * @return
     */
    public static Date dateTimeStr2date(String dateTime, String from, String to) {
        Date result = null;
        Date date = String2Date(dateTime, from);
        String resultstr = date2String(date, to);
        result = String2Date(resultstr, to);
        return result;
    }

    /**
     * 字符串转为日期
     *
     * @param strDate       日期字符串
     * @param strDateFormat 日期格式，时间类格式：yyyy-MM-dd HH:mm:ss.SSS / yyyyMMdd /yyyyMMdd HH:mm:ss
     * @return
     */
    public static Date String2Date(String strDate, String strDateFormat) {
        Date date = null;
        try {
            date = new SimpleDateFormat(strDateFormat).parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取某一个日期的年份
     *
     * @param date
     * @return
     */
    public static int getYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    /**
     * 获取某一个日期的月份
     *
     * @param date
     * @return
     */
    public static int getMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取某一个月的日
     *
     * @param date
     * @return
     */
    public static int getDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 根据日期确定星期几:1-星期日，2-星期一.....s
     *
     * @param date
     * @return
     */
    public static int getDayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int mydate = c.get(Calendar.DAY_OF_WEEK);
        return mydate;
    }

    /**
     * 获取某一天的小时
     *
     * @param date
     * @return
     */
    public static int getHourOfDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取某一日期的小时，1到12小时
     *
     * @param date
     * @return
     */
    public static int getHour(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.HOUR);
    }

    /**
     * 获取某一个日期的分钟
     *
     * @param date
     * @return
     */
    public static int getMinute(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MINUTE);
    }

    /**
     * 获取某一个日期的秒
     *
     * @param date
     * @return
     */
    public static int getSecond(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.SECOND);
    }

    public static String[] getYmdhm(String date) {
        int size = 5;
        String[] datea = new String[size];
        datea[0] = date.substring(0, 4);
        datea[1] = date.substring(4, 6);
        datea[2] = date.substring(6, 8);
        datea[3] = date.substring(8, 10);
        datea[4] = date.substring(10, 12);

        return datea;
    }

    public static String[] getYmdhms(String date) {
        int size = 6;
        String[] datea = new String[size];
        datea[0] = date.substring(0, 4);
        datea[1] = date.substring(4, 6);
        datea[2] = date.substring(6, 8);
        datea[3] = date.substring(8, 10);
        datea[4] = date.substring(10, 12);
        datea[5] = date.substring(12, 14);
        return datea;
    }

    /**
     * 从0000-01-01 00：00：00到今天的天数，转换成时间戳
     * @param days
     * @return
     */
    public static Date getYmdhms(double days) throws ParseException {
        int intDays=(int)days;
        double dhours=24*(days-intDays);
        int intHour=(int)(24*(days-intDays));

        double dMinute=60*(dhours-intHour);//////转换成分钟（带小数）
        int intMinute=(int)dMinute;

        double dSecond=60*(dMinute-intMinute);
        int intSecond=(int)dSecond;

        Date date = new Date();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        date = format.parse("0000-01-01 00:00:00");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH,intDays);
        calendar.add(Calendar.HOUR,intHour);
        calendar.add(Calendar.MINUTE,intMinute);
        calendar.add(Calendar.SECOND,intSecond);

        return calendar.getTime();
    }
}
