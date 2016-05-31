package com.desmond.demo.box.model;

import java.util.Date;

/**
 * Created by wangzunhui on 2016/5/30.
 */
public class DrugBox {
    private Integer id;
    private String name;
    private String company;
    private String otc;
    private String time;
    private String code;
    private String form;

    public String getCode() {
        return "国药准字" + code;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
