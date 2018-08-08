package com.wannengyongyao.drug.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class DrugSeller {
    private Long id;

    private String name;

    private Integer gender;

    private String mobile;

    private String idNumber;

    private String job;

    private String classify;

    private BigDecimal accountBalance;

    private String remark;

    private Date createTime;

    private String createIp;

    private Date lastUpdatedTime;

    private Integer status;

    private Integer receiveStatus;

    private Integer storeId;
}