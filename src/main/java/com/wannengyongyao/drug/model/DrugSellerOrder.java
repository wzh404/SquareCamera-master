package com.wannengyongyao.drug.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class DrugSellerOrder {
    private Integer id;

    private Long orderId;

    private Long userId;

    private String userName;

    private Long sellerId;

    private String sellerName;

    private String drugStore;

    private BigDecimal amount;

    private Date createTime;

    private Integer status;
}