package com.desmond.demo.plan.model;

import com.desmond.demo.box.model.TimeAndDosage;
import com.desmond.demo.common.util.DateUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by wangzunhui on 2016/6/3.
 */
public class DrugPlan extends RealmObject{
    @PrimaryKey
    private Long id;
    private Integer drugId;
    private String user;
    private String interval; // temp:一次性,  everyday:每日,  week:每周, days: 间隔天, hours: 间隔小时
    private String intervalDetails; // temp: null,  everyday:null,  week:246, days: 4, hours: 8
    private String dosages; // JSON格式服用时间、剂量、单位
    private Date startDate; // 开始服药日期
    private Date closeDate; // 结束日期
    private Integer days; // 服药持续天数

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public Integer getDrugId() {
        return drugId;
    }

    public void setDrugId(Integer drugId) {
        this.drugId = drugId;
    }

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

    public  String getDaysDesc(){
        HashMap<Integer, String> map = new HashMap<Integer,String>();
        map.put(-1, "按剩余剂量服用");
        map.put(-2, "持续服用");
        map.put(3, "三天");
        map.put(7, "一周");
        map.put(30, "一月");

        if (map.get(days) != null){
            return map.get(days);
        }

        return days + "天";
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

            return desc + ",每次" + dosage + unit;
        }
        else if ("hours".equalsIgnoreCase(interval)){
            int dosage = jsonElement.getAsJsonObject().get("dosages").getAsInt();
            String unit = jsonElement.getAsJsonObject().get("unit").getAsString();

            return "每次" + dosage + unit;
        }

        return "无";
    }

    public void setDefaultDosageOfDay(String unit){
        List<TimeAndDosage> list = new ArrayList<TimeAndDosage>();
        list.add(new TimeAndDosage("08:00", 2, unit));
        list.add(new TimeAndDosage("12:00", 2, unit));
        list.add(new TimeAndDosage("16:00", 2, unit));

        Gson gson = new Gson();
        setDosages(gson.toJson(list));
    }

    public void setDosageOfTemp(String time, Integer dosage, String unit){
        TimeAndDosage timeAndDosage = new TimeAndDosage(time, dosage, unit);
        setDosages((new Gson()).toJson(timeAndDosage));
    }

    public void setDefaultDosageOfTemp(String unit){
        setDosageOfTemp("12:00", 2, unit);
    }

    public void setDefaultDosageOfHours(String unit){
        TimeAndDosage timeAndDosage = new TimeAndDosage("no", 2, unit);
        setDosages((new Gson()).toJson(timeAndDosage));
    }

    public String getRestOfDay(){
        int day = DateUtil.getDay(closeDate);
        if (day <= 0){
            return "已结束";
        }
        else{
            return "剩余" + day + "天";
        }
    }
}
