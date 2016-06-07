package com.desmond.demo.box.model;

import com.desmond.demo.common.util.DateUtil;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by wangzunhui on 2016/5/30.
 */
public class Drug extends RealmObject{
    @PrimaryKey
    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("manufacturer")
    private String company;

    @SerializedName("otc")
    private String otc;

    @SerializedName("time")
    private Date time;

    @SerializedName("code")
    private String code;

    @SerializedName("form")
    private String form;

    @SerializedName("category")
    private String category;

    // 同步标识
    private boolean sync;

    // allow,deny
    private String reserve;

    private String state;

    public String showTime() {
       return DateUtil.formatDate(time);
    }

    public String showCode() {
        return "国药准字" + code;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getOtc() {
        return otc;
    }

    public void setOtc(String otc) {
        this.otc = otc;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public String getReserve() {
        return reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
