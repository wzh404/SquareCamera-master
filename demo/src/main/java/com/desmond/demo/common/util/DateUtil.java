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
    public static String getCurrentDate(){
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(dt);
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
