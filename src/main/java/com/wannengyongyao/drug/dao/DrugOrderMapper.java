package com.wannengyongyao.drug.dao;

import com.wannengyongyao.drug.model.DrugOrder;
import com.wannengyongyao.drug.model.DrugOrderShare;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface DrugOrderMapper {
    /**
     * 新增订单
     *
     * @param order
     * @return
     */
    int insert(DrugOrder order);

    /**
     * 获取订单状态
     *
     * @param orderId
     * @return
     */
    DrugOrder getOrderStatus(@Param("orderId")Long orderId);

    /**
     *
     * @param orderId
     * @return
     */
    DrugOrder getSettleOrder(@Param("orderId")Long orderId);

    /**
     * 改变订单状态
     *
     * @param orderId
     * @param status
     * @return
     */
    int changeOrderStatus(@Param("orderId")Long orderId, @Param("status")Integer status);

    /**
     * 用户订单列表
     *
     * @param conditionMap
     * @return
     */
    List<DrugOrder> list(Map<String, Object> conditionMap);

    /**
     * 订单详情
     *
     * @param orderId
     * @return
     */
    DrugOrder orderDetail(@Param("orderId")Long orderId);

    /**
     * 订单确认
     *
     * @param order
     * @return
     */
    int confirm(DrugOrder order);

    /**
     * 订单发货
     *
     * @param order
     * @return
     */
    int sellerShipping(DrugOrder order);

    /**
     * 用户取货确认
     *
     * @param order
     * @return
     */
    int sellerCollection(DrugOrder order);

    /**
     * 卖家待抢订单列表
     *
     * @param conditionMap
     * @return
     */
    List<DrugOrder> listSellerGrab(Map<String, Object> conditionMap);

    /**
     * 卖家订单列表
     *
     * @param conditionMap
     * @return
     */
    List<DrugOrder> listSeller(Map<String, Object> conditionMap);

    /**
     * 待取货订单列表
     *
     * @param conditionMap
     * @return
     */
    List<DrugOrder> listCollection(Map<String, Object> conditionMap);

    /**
     * 增加订单抢购人数
     *
     * @param id
     * @return
     */
    int increaseSellerNum(@Param("id")Long id);

    /**
     * 更改订单结算状态
     *
     * @param id
     * @return
     */
    int changeOrderSettlementStatus(@Param("id")Long id);

    /**
     * 查询未结算的订单
     *
     * @return
     */
    List<DrugOrder> listUnSettlementOrder();

    /**
     *
     * @param sellerId
     * @return
     */
    int getSellerOrderTotal(@Param("sellerId")Long sellerId);

    /**
     * 增加悬赏金额
     *
     * @param id
     * @param amount
     * @return
     */
    int addRewardAmount(@Param("id")Long id, @Param("amount")BigDecimal amount);

    /**
     *
     * @param share
     * @return
     */
    int insertOrderShare(DrugOrderShare share);

    /**
     *
     * @param orderId
     * @param openid
     * @return
     */
    int changeOrderShareNum(@Param("orderId")Long orderId, @Param("openid")String openid);

    /**
     *
     * @param orderId
     * @param openid
     * @return
     */
    Integer getOrderShareUserNum(@Param("orderId")Long orderId, @Param("openid")String openid);
    /**
     *
     * @param orderId
     * @return
     */
    List<DrugOrderShare> listOrderShareUsers(@Param("orderId")Long orderId);

    /**
     *
     * @param orderId
     * @return
     */
    Integer getOrderShareTotal(@Param("orderId")Long orderId);

    /**
     * 药店代收药品
     *
     * @param orderId
     * @return
     */
    int collectionOrder(@Param("orderId")Long orderId);

    /**
     * 订单已支付
     *
     * @param orderId
     * @return
     */
    int payment(@Param("orderId")Long orderId);
}