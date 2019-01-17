package com.mhwang.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Description :
 * Author :mhwang
 * Date : 2017/5/25
 * Version : V1.0
 */

public class DateUtil {

    public static final int DAY = 86400;
    public static final int HOUR = 3600;
    public static final int MINUTE = 60;

    /** 获取开机时间
     * @param time
     * @return
     */
    public static String parseOpenTime(String time){
        long lTimes = Long.parseLong(time,16);
        StringBuilder builder = new StringBuilder();
        // 获取天
        long days = lTimes / DAY;
        // 获取小时   总数/3600得到总小时数，去掉整数部分就是剩下小时部分
        long hours = (lTimes / HOUR) % 24;
        // 获取分  总数/60得到总分钟数，去掉整数部分就是剩下分钟部分
        long minutes = (lTimes / MINUTE) % 60;
        // 获取秒  对于秒，直接求余
        long seconds = lTimes % MINUTE;
        builder.append(days)
                .append("天")
                .append(hours)
                .append("小时")
                .append(minutes)
                .append("分")
                .append(seconds)
                .append("秒");
        return builder.toString();
    }

    public static String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        StringBuilder builder = new StringBuilder();
        builder.append(year);
        builder.append((month >= 10 ) ? month : "0" + month);
        builder.append((day >= 10 ) ? day : "0" + day);
        return builder.toString();
    }

    public static String getCurrentTime(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);
        return sdf.format(date);
    }

    public static String getTime(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        return sdf.format(date);
    }

    /** 获取月日
     * @return
     */
    public static String getMonthDay(){
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        StringBuilder builder = new StringBuilder();
        builder.append(month >= 10 ? month : "0"+month);
        builder.append("-");
        builder.append(day >= 10 ? day : "0"+day);
        return builder.toString();
    }

    /** 获取星期
     * @return
     */
    public static String getDayOfWeek(){
        String[] weekDays = new String[]{"S U N","M O N","T U E","W E D","T H U","F R I","S A T"};
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        return weekDays[dayOfWeek-1];
    }

    public static String getTimeForFile(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm", Locale.CHINA);
        return sdf.format(date);
    }

}
