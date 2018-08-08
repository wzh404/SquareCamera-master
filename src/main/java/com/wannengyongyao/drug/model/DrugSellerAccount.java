package com.wannengyongyao.drug.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class DrugSellerAccount {
    private Integer id;

    private Integer sellerId;

    private Long orderId;

    private BigDecimal amount;

    private Date createTime;

    private Integer debit;

    private String remark;
}