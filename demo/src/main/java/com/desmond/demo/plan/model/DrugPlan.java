package com.desmond.demo.plan.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.Date;
import java.util.HashMap;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by wangzunhui on 2016/6/3.
 */
public class DrugPlan {
//    @PrimaryKey
    private Long id;
    private String user;
    private String interval; // temp:一次性,  everyday:每日,  week:每周, days: 间隔天, hours: 间隔小时
    private String intervalDetails;
    private String dosages; // JSON格式服用时间及剂量、单位
    private Date startDate; // 开始服药日期
    private Integer days; // 服药持续天数

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getIntervalDetails() {
        return intervalDetails;
    }

    public void setIntervalDetails(String intervalDetails) {
        this.intervalDetails = intervalDetails;
    }

    public String getDosages() {
        return dosages;
    }

    public void setDosages(String dosages) {
        this.dosages = dosages;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getIntervalDesc(){
        HashMap<String, String> map = new HashMap<String,String>();
        map.put("temp", "一次性");
        map.put("everyday", "每日");
        map.put("week", "每周");
        map.put("days", "间隔天");
        map.put("hours", "间隔小时");

        return map.get(interval);
    }

    public String getDosageDesc(){
        Gson gson = new Gson();
        JsonElement jsonElement = gson.fromJson(dosages, JsonElement.class);
        if ("everyday".equalsIgnoreCase(interval) ||
            "days".equalsIgnoreCase(interval) ||
            "week".equalsIgnoreCase(interval)){
            int size = jsonElement.getAsJsonArray().size();
            String desc = "每日" + size + "次, 每次";

            int min = 0, max = 0;
            String unit = "";
            for (JsonElement element : jsonElement.getAsJsonArray()){
                unit = element.getAsJsonObject().get("unit").getAsString();

                int dosage = element.getAsJsonObject().get("dosages").getAsInt();
                if (dosage > max || max == 0) max = dosage;
                if (dosage < min || min == 0) min = dosage;
            }

            if (min == max)
                desc += (min + unit);
            else
                desc += (min + "~" + max + unit);

            return desc;
        }
        else if ("temp".equalsIgnoreCase(interval)){
            String desc = jsonElement.getAsJsonObject().get("time").getAsString();
            int dosage = jsonElement.getAsJsonObject().get("dosages").getAsInt();
            String unit = jsonElement.getAsJsonObject().get("unit").getAsString();

            return desc + "," + dosage + unit;
        }
        else if ("hours".equalsIgnoreCase(interval)){
            int dosage = jsonElement.getAsJsonObject().get("dosages").getAsInt();
            String unit = jsonElement.getAsJsonObject().get("unit").getAsString();

            return "每次" + dosage + unit;
        }

        return "无";
    }
}
