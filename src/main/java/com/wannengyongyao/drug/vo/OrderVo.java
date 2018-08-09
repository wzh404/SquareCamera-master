package com.wannengyongyao.drug.vo;

import com.wannengyongyao.drug.common.status.OrderStatus;
import com.wannengyongyao.drug.model.DrugOrder;
import com.wannengyongyao.drug.model.DrugOrderGoods;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderVo {
    private Long userId;
    // 预估金额
    private BigDecimal expectedAmount;
    // 服务费
    private BigDecimal serviceCharge;
    // 悬赏金额
    private BigDecimal rewardAmount;
    // 运费
    private BigDecimal freight;
    // 代收药店
    private Integer storeId;

    // 药品信息
    List<DrugVo> drugs;

    // 订单信息
    public DrugOrder asOrder(){
        DrugOrder order = new DrugOrder();
        order.setOrderStatus(OrderStatus.INIT.get());
        order.setCreateTime(LocalDateTime.now());
        order.setExpectedAmount(this.expectedAmount);
        order.setSellerStoreId(this.storeId);
        order.setServiceCharge(this.serviceCharge);
        order.setFreight(this.freight);
        order.setRewardAmount(this.rewardAmount);

        order.setDiscountAmount(new BigDecimal(0.0));
        order.setPayment("weixin");
        order.setSellerNum(0);
        order.setUserId(this.userId);
        order.setLogisticsStatus(0);

        return order;
    }

    // 订单药品信息
    public List<DrugOrderGoods> asGoods(){
        List<DrugOrderGoods> goods = new ArrayList<>();
        for (DrugVo d : drugs){
            DrugOrderGoods g = new DrugOrderGoods();
            g.setDrugId(d.getId());
            g.setQuantity(d.getQuantity());

            goods.add(g);
        }

        return goods;
    }
}
