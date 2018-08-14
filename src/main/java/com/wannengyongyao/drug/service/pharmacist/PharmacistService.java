package com.wannengyongyao.drug.service.pharmacist;

import com.wannengyongyao.drug.model.*;
import com.wannengyongyao.drug.vo.PharmacistOrderVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface PharmacistService {
    /**
     * 根据手机号查询药师信息
     *
     * @param mobile
     * @return
     */
    DrugSeller getPharmacitsByMobile(String mobile);

    /**
     * 根据id获取卖家信息
     *
     * @param id
     * @return
     */
    DrugSeller getSeller(Long id);

    /**
     * 根据订单号获取订单基本信息
     *
     * @param orderId
     * @return
     */
    DrugOrder getOrderStatus(Long orderId);

    /**
     * 卖家注册
     *
     * @param seller
     * @return
     */
    int insert(DrugSeller seller);

    /**
     * 卖家修改信息
     *
     * @param seller
     * @return
     */
    int update(DrugSeller seller);

    /**
     * 发送报价
     *
     * @param orderVo
     * @param sellerId
     * @return
     */
    int grab(PharmacistOrderVo orderVo, long sellerId);

    /**
     * 卖家待抢订单列表
     *
     * @param conditionMap
     * @return
     */
    List<DrugOrder> listSellerGrab(Map<String, Object> conditionMap);

    /**
     * 卖家订单列表(已发货，已完成，待发货，待取货）
     *
     * @param conditionMap
     * @return
     */
    List<DrugOrder> listSeller(Map<String, Object> conditionMap);

    /**
     * 待确认订单列表
     *
     * @param conditionMap
     * @return
     */
    List<DrugSellerOrder> listSellerUnconfirmed(Map<String, Object> conditionMap);

    /**
     * 待取货订单列表
     *
     * @param conditionMap
     * @return
     */
    List<DrugOrder> listCollection(Map<String, Object> conditionMap);

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
     * 药师结交的用户列表
     *
     * @param pharmacistId
     * @return
     */
    List<DrugUser> getPharmacistUsers(Long pharmacistId);

    /**
     * 用户收入
     *
     * @param sellerId
     * @param orderId
     * @param amount
     * @return
     */
    int income(Long sellerId, Long orderId, BigDecimal amount, String remark);
}
