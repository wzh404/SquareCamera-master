package com.wannengyongyao.drug.model;

import lombok.Data;


import java.util.Date;

@Data
public class DrugUser {
    private Long id;

    private String name;

    private Integer gender;

    private String mobile;

    private String remark;

    private Date createTime;

    private String createIp;

    private Date lastUpdatedTime;

    private Integer status;
}