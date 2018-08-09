package com.wannengyongyao.drug.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    private LocalDateTime payTime;

    private LocalDateTime shippingTime;

    private LocalDateTime signTime;

    private LocalDateTime createTime;

    private LocalDateTime confirmTime;

    private Integer orderStatus;

    private Integer logisticsStatus;

    private String shippingCompany;

    private String shippingId;

    // 抢单数
    private Integer sellerNum;

    private String collectionStore;

    private String contacts;
}