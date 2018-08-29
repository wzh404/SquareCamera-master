package com.wannengyongyao.drug.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DrugOrder  {
    private Long id;

    private Long userId;

    private String userName;

    private Long sellerId;

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime payTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime shippingTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime signTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime confirmTime;

    private Integer orderStatus;

    private Integer shippingStatus;

    private Integer payStatus;

    private String shippingCompany;

    private String shippingId;

    // 抢单数
    private Integer sellerNum;

    // 订单结算状态
    private Integer settlementStatus;

    // 代收人情况
    private String collectionStore;

    // 收货码
    private String collectionCode;

    private Integer collectionStoreId;

    private String couponCode;

    // 用户地址
    private String address;

    private String mobile;

    // 代收时间
    private String collectionTime;

    private List<DrugOrderGoods> drugs;

    public BigDecimal getPayAmount(){
        if (this.orderAmount == null || freight == null || serviceCharge == null || rewardAmount == null || discountAmount == null){
            return BigDecimal.valueOf(0.0);
        }

        return this.orderAmount
                .add(this.serviceCharge)
                .add(freight)
                .add(rewardAmount)
                .subtract(discountAmount);
    }
}