package com.wannengyongyao.drug.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class DrugOrder  {
    private Long id;

    private Long userId;

    private String userName;

    private Integer sellerId;

    private String sellerName;

    private Integer sellerStoreId;

    private String sellerStoreName;

    private String payment;

    private BigDecimal payAmount;

    private BigDecimal orderAmount;

    private BigDecimal rewardAmount;

    private BigDecimal discountAmount;

    private BigDecimal expectedAmount;

    private BigDecimal serviceCharge;

    private BigDecimal freight;

    private String shippingWay;

    private Date payTime;

    private Date shippingTime;

    private Date signTime;

    private Date createTime;

    private Date confirmTime;

    private Integer orderStatus;

    private Integer logisticsStatus;

    private String shippingCompany;

    private String shippingId;

    private Integer sellerNum;

    private String collectionStore;

    private String contacts;
}