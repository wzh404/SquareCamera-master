package com.wannengyongyao.drug.vo;

import com.wannengyongyao.drug.common.status.OrderStatus;
import com.wannengyongyao.drug.common.status.ShippingStatus;
import com.wannengyongyao.drug.model.DrugOrder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderVo {
    @Min(1)
    private Long userId;
    // 预估金额
    @NotNull(message="预估金额不能为空")
    private BigDecimal expectedAmount;
    // 服务费
    @NotNull(message="服务费不能为空")
    private BigDecimal serviceCharge;
    // 悬赏金额
    private BigDecimal rewardAmount;
    // 运费
    @NotNull(message="运费不能为空")
    private BigDecimal freight;
    // 优惠券
    private String coupon;
    // 用户地址
    private String address;

    // 代收药店
    @Min(1)
    private Integer storeId;

    // 订单信息
    public DrugOrder asOrder(){
        DrugOrder order = new DrugOrder();
        order.setOrderStatus(OrderStatus.INIT.get());
        order.setCreateTime(LocalDateTime.now());
        order.setExpectedAmount(this.expectedAmount);
        order.setCollectionStoreId(this.storeId);
        order.setServiceCharge(this.serviceCharge);
        order.setFreight(this.freight);
        order.setRewardAmount(this.rewardAmount == null ? new BigDecimal(0.0) : this.rewardAmount);
        order.setAddress(this.address);

        order.setDiscountAmount(new BigDecimal(0.0));
        order.setPayment("weixin");
        order.setSellerNum(0);
        order.setUserId(this.userId);
        order.setShippingStatus(ShippingStatus.INIT.get());

        return order;
    }
}
