package com.wannengyongyao.drug.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class DrugStore {
    private Integer id;

    private String name;

    private String classify;

    private String companyAddress;

    private String province;

    private String city;

    private String district;

    private String remark;

    private Date createTime;

    private Date lastUpdatedTime;

    private Integer status;
}