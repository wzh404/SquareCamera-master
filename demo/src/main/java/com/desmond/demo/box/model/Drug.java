package com.desmond.demo.box.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.desmond.demo.common.util.DateUtil;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by wangzunhui on 2016/5/30.
 */
public class Drug extends RealmObject implements Parcelable{
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
    private String code; // 准字号

    @SerializedName("form") // 剂型
    private String form;

    @SerializedName("category")
    private String category; // 分类 中药，化学药品

    // 同步标识
    private boolean sync;

    // allow,deny
    private String reserve;

    private String state; // 药品状态

    private String meal; // 餐饮说明

    private String dosage; // 剂量单位

    private Integer stock; // 库存量

    private String icon; //图标

    public Drug(){}

    protected Drug(Parcel in) {
        id = in.readInt();
        name = in.readString();
        company = in.readString();
        otc = in.readString();
        code = in.readString();
        form = in.readString();
        category = in.readString();
        sync = in.readByte() != 0;
        reserve = in.readString();
        state = in.readString();
        meal = in.readString();
        dosage = in.readString();
        stock = in.readInt();
        icon = in.readString();
    }

    public static final Creator<Drug> CREATOR = new Creator<Drug>() {
        @Override
        public Drug createFromParcel(Parcel in) {
            return new Drug(in);
        }

        @Override
        public Drug[] newArray(int size) {
            return new Drug[size];
        }
    };

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

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(company);
        dest.writeString(otc);
        dest.writeString(code);
        dest.writeString(form);
        dest.writeString(category);
        dest.writeByte((byte) (sync ? 1 : 0));
        dest.writeString(reserve);
        dest.writeString(state);
        dest.writeString(meal);
        dest.writeString(dosage);
        dest.writeInt(stock);
        dest.writeString(icon);
    }
}
