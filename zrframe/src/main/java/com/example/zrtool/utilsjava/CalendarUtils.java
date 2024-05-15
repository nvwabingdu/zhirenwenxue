package com.example.zrtool.utilsjava;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2022-06-17
 * Time: 15:50
 */
public class CalendarUtils {

    /**
     * 无用的
     */
    public static void getTime(){
         SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //获取calendar实例;
        Calendar calendar = Calendar.getInstance();

        //判断calendar是不是GregorianCalendar类的实例;
        if (calendar instanceof GregorianCalendar) {
            System.out.println("属于GregorianCalendar类的实例!");
        }

        //从calendar对象中获得date对象，当前时间;
        Date dates = calendar.getTime();

        //格式化时间;
        String date_str = date_format.format(dates);
        System.out.println(date_str);

        //设置月份05；代表日历的月份6月，因为月份从0开始。
        calendar.set(Calendar.MONTH, 05);

        int months = calendar.get(Calendar.MONTH);
        System.out.println(months);  //输出05;

        //设置日期为2011-07-24 09：59:50
        calendar.set(2011, 06, 24, 9, 59, 50);
        String getDate = date_format.format(calendar.getTime());
        System.out.println(getDate);   //输出2011-07-24 09:59:50;

        //比较日前大小;
        if (new Date().getTime() > calendar.getTimeInMillis()) {
            System.out.println("当前日期在后!");
        } else {
            System.out.println("当前日期在前!");
        }

        //设置当前时间为:2011-07-24 11:06:00
        calendar.setTime(new Date());
    }

    /**
     * 获取日历的年份
     * @return
     */
    public static int getCalendar_YEAR(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取日历的月份 要加1
     * @return
     */
    public static int getCalendar_MONTH(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.MONTH)+1;
    }

    /**
     * 获取日历的天
     * @return
     */
    public static int getCalendar_DATE(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.DATE);
    }

    /**
     * 获取日历的小时
     * @return
     */
    public static int getCalendar_HOUR(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.HOUR);
    }

    /**
     * 获取日历的分
     * @return
     */
    public static int getCalendar_MINUTE(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 获取日历的秒
     * @return
     */
    public static int getCalendar_SECOND(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.SECOND);
    }

    /**
     * 获取日历的第几个小时
     * @return
     */
    public static int getCalendar_HOUR_OF_DAY(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取日历的这天，在一个月内是第几天
     * @return
     */
    public static int getCalendar_DAY_OF_MONTH(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取日历的这天，在一年内，是第几天
     * @return
     */
    public static int getCalendar_DAY_OF_YEAR(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 获取日历的这天，在一周内，是第几天
     * @return
     */
    public static int getCalendar_DAY_OF_WEEK(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取日历的这周，在一年内是第几周
     * @return
     */
    public static int getCalendar_WEEK_OF_YEAR(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取日历的这周，在这个月是第几周
     * @return
     */
    public static int getCalendar_WEEK_OF_MONTH(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

    /**
     * 获取日历的时区
     * 获取TimeZone可以通过时区ID，如"America/New_York"，
     * 也可以通过GMT+/-hh:mm来设定。例如北京时间可以表示为GMT+8:00
     * @return
     */
    public static int getCalendar_ZONE_OFFSET(){
    //TimeZone.setDefault(TimeZone.getTimeZone(Zone.ZONE_ASIA_SHANGHAI));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.ZONE_OFFSET);
    }

    /**
     * 获取日历的某月中第几周，按这个月1号算，1号起就是第1周，8号起就是第2周。以月份天数为标准
     * @return
     */
    public static int getCalendar_DAY_OF_WEEK_IN_MONTH(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
    }

    /**
     * 获取日历的AM_PM (0为AM，1为PM)  calendar.AM   calendar.PM
     * @return
     */
    public static int getCalendar_AM_PM(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.AM_PM);
    }

    /**
     * 获取日历的格式化时间 默认：
     *                        2022-06-17 06:09:20
     *
     * @param formatStr "yyyy_MM_dd HH_mm_ss_SS"
     * @param timestamp  System.currentTimeMillis()
     *                   Calendar.getInstance().getTimeInMillis()
     *                   new Date().getTime()
     * @return
     */
    public static String getCalendarFormatTime(String formatStr,long timestamp){
        SimpleDateFormat dateFormat;
        if(formatStr!=""){
             dateFormat = new SimpleDateFormat(formatStr);
        }else {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        }
        //String date_str= dateFormat.format(Calendar.getInstance().getTime());
        String date_str= dateFormat.format(timestamp);
        return date_str;
    }

    /**
     * 获取时间戳 13位 1655461596423
     * @return
     */
    public static long getTimestamp(){
        return System.currentTimeMillis();//耗时100
        // return Calendar.getInstance().getTimeInMillis();//耗时2000
        // return new Date().getTime();//耗时101
    }





}
