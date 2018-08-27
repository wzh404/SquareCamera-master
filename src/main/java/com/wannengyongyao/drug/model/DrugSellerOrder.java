package com.wannengyongyao.drug.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DrugSellerOrder {
    private Integer id;

    private Long orderId;

    private Long userId;

    private String userName;

    private Long sellerId;

    private String sellerName;

    private String drugStore;

    private Integer drugStoreId;

    private BigDecimal amount;

    private LocalDateTime createTime;

    private Integer status;

    // order
    private String collectionStore;

    private Integer collectionStoreId;

    private BigDecimal rewardAmount;

    private BigDecimal discountAmount;

    private BigDecimal expectedAmount;

    private BigDecimal serviceCharge;

    private BigDecimal freight;

    private String address;

    private List<DrugSellerOrderGoods> drugs;
}