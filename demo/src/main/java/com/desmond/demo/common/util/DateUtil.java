package com.desmond.demo.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by wangzunhui on 2016/5/31.
 */
public class DateUtil {
    /**
     * 获取当前日期yyyymmdd
     *
     * @return
     */
    public static String getCurrentDatetime(){
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(dt);
    }

    public static String getDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 获取当前时间HHMM
     *
     * @return
     */
    public static String getCurrentTime(){
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(dt);
    }

    public static String getMonthDay(Date date){
        return (new SimpleDateFormat("MM-dd")).format(date);
    }

    public static Date addDate(Date start, int days){
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(start);
        calendar.add(GregorianCalendar.DAY_OF_MONTH, days);

        return calendar.getTime();
    }

    public static int getDay(Date closeDate){
        return diffDay(new Date(), closeDate);
    }

    public static int getCurrentWeek(){
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.setFirstDayOfWeek(Calendar.MONDAY);

        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (0 == week) {
            week = 7;
        }
        return week;
    }

    public static int diffDay(Date startDate, Date closeDate){
        if (closeDate == null)
            return 0;

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(closeDate);
        int day1 = calendar.get(Calendar.DAY_OF_YEAR);

        calendar.setTime(startDate);
        int day2 = calendar.get(Calendar.DAY_OF_YEAR);

        return day1 - day2;
    }

    public static String formatDate(Date date){
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        GregorianCalendar currentCalendar = new GregorianCalendar();
        currentCalendar.setTime(new Date());

        int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = currentCalendar.get(Calendar.MONTH);
        int currentYear = currentCalendar.get(Calendar.YEAR);

        if (currentYear != year || currentMonth != month || currentDay != day){
            SimpleDateFormat history = new SimpleDateFormat("MM-dd");
            return history.format(date);
        }

        SimpleDateFormat today = new SimpleDateFormat("HH:mm");
        return today.format(date);
    }
}
