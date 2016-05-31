package com.desmond.demo.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

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
}
