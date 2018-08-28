package com.wannengyongyao.drug.service.user;

import com.wannengyongyao.drug.common.ResultCode;
import com.wannengyongyao.drug.model.DrugOrder;
import com.wannengyongyao.drug.model.DrugOrderShare;
import com.wannengyongyao.drug.model.DrugSellerOrder;
import com.wannengyongyao.drug.vo.DrugOrderVo;
import com.wannengyongyao.drug.vo.PhotoOrderVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface DrugOrderService {
    /**
     * 用户下单
     *
     * @param orderVo
     * @return
     */
    Integer newOrder(DrugOrderVo orderVo);

    /**
     * 拍照下单
     *
     * @param orderVo
     * @return
     */
    Integer newPhotoOrder(PhotoOrderVo orderVo);

    /**
     * 取消订单
     *
     * @param userId
     * @param orderId
     * @return
     */
    int cancelOrder(Long userId, Long orderId);

    /**
     * 确认订单
     *
     * @param userId
     * @param orderId
     * @param sellerId
     * @return
     */
    ResultCode confirmOrder(Long userId, Long orderId, Long sellerId);

    /**
     * 订单列表
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
    DrugOrder orderDetail(Long orderId);

    /**
     * 订单报价列表
     *
     * @param conditionMap
     * @return
     */
    List<DrugSellerOrder> listByOrder(Map<String, Object> conditionMap);

    /**
     * 根据订单号获取订单基本信息
     *
     * @param orderId
     * @return
     */
    DrugOrder getOrderStatus(Long orderId);

    /**
     * 增加悬赏金额
     *
     * @param orderId
     * @param amount
     * @return
     */
    int addRewardAmount(Long orderId, BigDecimal amount);

    /**
     *
     * @param share
     * @return
     */
    int insertOrderShare(DrugOrderShare share);

    /**
     *
     * @param orderId
     * @return
     */
    List<Map<String, Object>> getOrderShare(Long orderId);
}
