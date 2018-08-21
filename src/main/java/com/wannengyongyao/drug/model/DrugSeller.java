package com.wannengyongyao.drug.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    private LocalDateTime createTime;

    private String createIp;

    private LocalDateTime lastUpdatedTime;

    private Integer status;

    private Integer receiveStatus;

    private Integer storeId;

    private String storeName;

    private Double distance;

    private String openid;
}