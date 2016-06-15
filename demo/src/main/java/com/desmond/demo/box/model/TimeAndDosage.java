package com.desmond.demo.box.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wangzunhui on 2016/6/15.
 */
public class TimeAndDosage {
    private String time;
    private Integer dosages;
    private String unit;

    public TimeAndDosage(String time, Integer dosages, String unit){
        this.time = time;
        this.dosages = dosages;
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Date toTime() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:MM");
        return sdf.parse(time);
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getDosages() {
        return dosages;
    }

    public void setDosages(Integer dosages) {
        this.dosages = dosages;
    }
}
