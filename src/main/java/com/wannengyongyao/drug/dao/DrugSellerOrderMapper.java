package com.wannengyongyao.drug.dao;

import com.wannengyongyao.drug.model.DrugSellerOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DrugSellerOrderMapper {
    int insert(DrugSellerOrder order);

    DrugSellerOrder getSellerOrder(@Param("orderId")Long orderId, @Param("sellerId")Long sellerId);

    /**
     * 更新卖家抢单状态为抢单成功
     *
     * @param orderId
     * @param sellerId
     * @return
     */
    int changeStatusConfirm(@Param("orderId")Long orderId, @Param("sellerId")Long sellerId);

    /**
     * 用户订单报价列表
     *
     * @param conditionMap
     * @return
     */
    List<DrugSellerOrder> listByOrder(Map<String, Object> conditionMap);

    /**
     * 卖家待确认订单列表
     *
     * @param conditionMap
     * @return
     */
    List<DrugSellerOrder> listSellerUnconfirmed(Map<String, Object> conditionMap);
}