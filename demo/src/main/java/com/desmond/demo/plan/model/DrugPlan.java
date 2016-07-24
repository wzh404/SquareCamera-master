package com.desmond.demo.plan.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.desmond.demo.box.model.Drug;
import com.desmond.demo.box.model.TimeAndDosage;
import com.desmond.demo.common.util.DateUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by wangzunhui on 2016/6/3.
 */
public class DrugPlan extends RealmObject implements Parcelable{
    @PrimaryKey
    private Long id;
    private Drug drug;
    private String user;
    private String interval; // temp:一次性,  everyday:每日,  week:每周, days: 间隔天, hours: 间隔小时
    private String intervalDetails; // temp: null,  everyday:null,  week:246, days: 4, hours: 8
    private String dosages; // JSON格式服用时间、剂量、单位
    private Date startDate; // 开始服药日期
    private Date closeDate; // 结束日期
    private Integer days; // 服药持续天数
    private String reason; // 服药原因
    private String state; // 状态

    public DrugPlan(){}
    protected DrugPlan(Parcel in) {
        id = in.readLong();
        drug = in.readParcelable(Drug.class.getClassLoader());
        user = in.readString();
        interval = in.readString();
        intervalDetails = in.readString();
        dosages = in.readString();
        startDate = (Date)in.readSerializable();
        closeDate = (Date)in.readSerializable();
        days = in.readInt();
        reason = in.readString();
        state = in.readString();
    }

    public static final Creator<DrugPlan> CREATOR = new Creator<DrugPlan>() {
        @Override
        public DrugPlan createFromParcel(Parcel in) {
            return new DrugPlan(in);
        }

        @Override
        public DrugPlan[] newArray(int size) {
            return new DrugPlan[size];
        }
    };

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public Drug getDrug() {
        return drug;
    }

    public void setDrug(Drug drug) {
        this.drug = drug;
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
        map.put("week", "每周" + intervalDetails);
        map.put("days", "间隔" + intervalDetails + "天");
        map.put("hours", "间隔" + intervalDetails + "小时");

        return map.get(interval);
    }

    /* 当天日期有效,过滤提醒计划 */
    public boolean filter(){
        final String[] weeks ={"一", "二", "三", "四", "五", "六", "日"};
        if ("week".equalsIgnoreCase(interval)){
            int week = DateUtil.getCurrentWeek();
            String weekText = weeks[week - 1];
            Log.e("Drug", weekText + "__" + intervalDetails);
            return intervalDetails.contains(weekText);
        }
        else if ("days".equalsIgnoreCase(interval)){
            int days = DateUtil.diffDay(startDate, new Date());
            int day = Integer.valueOf(intervalDetails);

            Log.e("Drug", day + "__" + days);
            return (days % day) == 0;
        }
        else if ("temp".equalsIgnoreCase(interval)){
            int days = DateUtil.diffDay(startDate, new Date());

            return days == 0;
        }

        return true;
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
            String desc = "一日" + size + "次, 每次";

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

    /**
     * 根据间隔小时计算当天的提醒时间
     */
    public void createCurrentReminder(TreeMap<String, List<String>> map){
        if (! filter()) return;

        if ("hours".equalsIgnoreCase(interval)) {
            Gson gson = new Gson();
            JsonElement jsonElement = gson.fromJson(dosages, JsonElement.class);
            int dosage = jsonElement.getAsJsonObject().get("dosages").getAsInt();
            String unit = jsonElement.getAsJsonObject().get("unit").getAsString();

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(new Date());
            int day = calendar.get(Calendar.DAY_OF_YEAR);

            calendar.setTime(startDate);
            int day0 = calendar.get(Calendar.DAY_OF_YEAR);

            while (day0 <= day) {
                calendar.add(Calendar.HOUR, Integer.valueOf(intervalDetails));
                day0 = calendar.get(Calendar.DAY_OF_YEAR);
                if (day == day0) {
                    String time = sdf.format(calendar.getTime());
                    List<String> list = map.get(time);
                    if (list == null){
                        list = new ArrayList<String>();
                    }
                    list.add(drug.getName() + "," + dosage + unit);
                    map.put(time, list);
                }
            }
        }
        else if ("everyday".equalsIgnoreCase(interval) ||
                 "week".equalsIgnoreCase(interval) ||
                 "days".equalsIgnoreCase(interval)){
            Gson gson = new Gson();
            JsonElement jsonElement = gson.fromJson(dosages, JsonElement.class);
            for (JsonElement element : jsonElement.getAsJsonArray()) {
                String time = element.getAsJsonObject().get("time").getAsString();
                String unit = element.getAsJsonObject().get("unit").getAsString();
                int dosage = element.getAsJsonObject().get("dosages").getAsInt();

                List<String> list = map.get(time);
                if (list == null){
                    list = new ArrayList<String>();
                }
                list.add(drug.getName() + "," + dosage + unit);
                map.put(time, list);
            }
        }
        else if ("temp".equalsIgnoreCase(interval)){
            Gson gson = new Gson();
            JsonElement element = gson.fromJson(dosages, JsonElement.class);

            String time = element.getAsJsonObject().get("time").getAsString();
            String unit = element.getAsJsonObject().get("unit").getAsString();
            int dosage = element.getAsJsonObject().get("dosages").getAsInt();

            List<String> list = map.get(time);
            if (list == null){
                list = new ArrayList<String>();
            }
            list.add(drug.getName() + "," + dosage + unit);
            map.put(time, list);
        }
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
        TimeAndDosage timeAndDosage = new TimeAndDosage(DateUtil.getCurrentTime(), 2, unit);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeParcelable(drug, flags);
        dest.writeString(user);
        dest.writeString(interval);
        dest.writeString(intervalDetails);
        dest.writeString(dosages);
        dest.writeSerializable(startDate);
        dest.writeSerializable(closeDate);
        dest.writeInt(days);
        dest.writeString(reason);
        dest.writeString(state);
    }
}
