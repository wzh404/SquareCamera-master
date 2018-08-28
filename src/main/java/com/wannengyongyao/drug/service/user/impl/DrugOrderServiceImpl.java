package com.wannengyongyao.drug.service.user.impl;

import com.wannengyongyao.drug.common.ResultCode;
import com.wannengyongyao.drug.common.status.CouponStatus;
import com.wannengyongyao.drug.common.status.OrderStatus;
import com.wannengyongyao.drug.dao.*;
import com.wannengyongyao.drug.model.*;
import com.wannengyongyao.drug.service.user.DrugOrderService;
import com.wannengyongyao.drug.util.StringUtil;
import com.wannengyongyao.drug.vo.DrugOrderVo;
import com.wannengyongyao.drug.vo.PhotoOrderVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("drugOrderService")
public class DrugOrderServiceImpl implements DrugOrderService {
    private final Logger logger = LoggerFactory.getLogger(DrugOrderServiceImpl.class);

    @Autowired
    private DrugUserMapper userMapper;

    @Autowired
    private DrugOrderMapper orderMapper;

    @Autowired
    private DrugMapper drugMapper;

    @Autowired
    private DrugStoreMapper drugStoreMapper;

    @Autowired
    private DrugOrderGoodsMapper drugOrderGoodsMapper;

    @Autowired
    private DrugSellerOrderMapper sellerOrderMapper;

    @Autowired
    private DrugSellerOrderGoodsMapper sellerOrderGoodsMapper;

    @Autowired
    private DrugSellerMapper sellerMapper;

    @Autowired
    private DrugCouponMapper couponMapper;

    /**
     * 下单
     *
     * @param orderVo
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Integer newOrder(DrugOrderVo orderVo) {
        // 订单用户信息
        DrugUser user = userMapper.get(orderVo.getUserId());
        DrugOrder order = orderVo.asOrder();
        order.setUserName(user.getName());

        // 优惠券
        if (orderVo.getCoupon() != null) {
            DrugUserCoupon coupon = couponMapper.getUserCoupon(orderVo.getCoupon());
            if (coupon == null ){
                return -2;
            }
            if (coupon.getStatus() != CouponStatus.NORMAL.get()){
                return -3;
            }
            if (coupon.getEndDate().isBefore(LocalDate.now())){
                return -4;
            }
            if (coupon.getStartDate().isAfter(LocalDate.now())){
                return -5;
            }

            order.setDiscountAmount(BigDecimal.valueOf(coupon.getAmount()));
            order.setCouponCode(orderVo.getCoupon());
            couponMapper.changeUserCouponStatus(orderVo.getCoupon(), CouponStatus.USED.get());
        } else {
            order.setDiscountAmount(BigDecimal.valueOf(0.0));
        }

        // 订单代收药店
        DrugStore store = drugStoreMapper.get(orderVo.getStoreId());
        order.setCollectionStore(store.getName());

        // 生成订单
        long orderId = StringUtil.getOrderId(orderVo.getUserId());
        order.setId(orderId);

        // 订单药品信息
        List<DrugOrderGoods> goods = orderVo.asGoods();
        for (DrugOrderGoods g : goods){
            Drug drug = drugMapper.get(g.getDrugId());
            g.setDrugName(drug.getName());
            g.setManufacturer(drug.getManufacturer());
            g.setSpecifications(drug.getSpecifications());
            g.setUnitPrice(BigDecimal.valueOf(0.0));
            g.setOrderId(orderId);
            g.setCreateTime(LocalDateTime.now());
        }
        int rows = drugOrderGoodsMapper.insert(goods);
        if (rows < 1){
            return -1;
        }

        // 自动添加我的代收药店
        int cnt = userMapper.getUserStoreCount(orderVo.getUserId(), orderVo.getStoreId());
        if (cnt < 1) {
            DrugUserStore userStore = new DrugUserStore();
            userStore.setUserId(orderVo.getUserId());
            userStore.setStoreId(orderVo.getStoreId());
            userStore.setCreateTime(LocalDateTime.now());
            userMapper.insertUserStore(userStore);
        }

        rows = orderMapper.insert(order);
        return rows;
    }

    /**
     * 拍照下单
     *
     * @param orderVo
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Integer newPhotoOrder(PhotoOrderVo orderVo) {
        // 订单用户信息
        DrugUser user = userMapper.get(orderVo.getUserId());
        DrugOrder order = orderVo.asOrder();
        order.setUserName(user.getName());

        // 优惠券
        if (orderVo.getCoupon() != null) {
            DrugUserCoupon coupon = couponMapper.getUserCoupon(orderVo.getCoupon());
            if (coupon == null ){
                return -2;
            }
            if (coupon.getStatus() != CouponStatus.NORMAL.get()){
                return -3;
            }
            if (coupon.getEndDate().isBefore(LocalDate.now())){
                return -4;
            }
            if (coupon.getStartDate().isAfter(LocalDate.now())){
                return -5;
            }

            order.setDiscountAmount(BigDecimal.valueOf(coupon.getAmount()));
            order.setCouponCode(orderVo.getCoupon());
            couponMapper.changeUserCouponStatus(orderVo.getCoupon(), CouponStatus.USED.get());
        } else {
            order.setDiscountAmount(BigDecimal.valueOf(0.0));
        }

        // 订单代收药店
        DrugStore store = drugStoreMapper.get(orderVo.getStoreId());
        order.setCollectionStore(store.getName());
        // 生成订单
        long orderId = StringUtil.getOrderId(orderVo.getUserId());
        order.setId(orderId);

        // 我的代收药店
        int cnt = userMapper.getUserStoreCount(orderVo.getUserId(), orderVo.getStoreId());
        if (cnt < 1) {
            DrugUserStore userStore = new DrugUserStore();
            userStore.setUserId(orderVo.getUserId());
            userStore.setStoreId(orderVo.getStoreId());
            userStore.setCreateTime(LocalDateTime.now());
            userMapper.insertUserStore(userStore);
        }

        // 订单拍照药品信息
        List<DrugOrderGoods> goods = orderVo.asGoods();
        for (DrugOrderGoods g : goods){
            g.setOrderId(orderId);
        }
        drugOrderGoodsMapper.insert(goods);

        return orderMapper.insert(order);
    }

    /**
     * 取消下单
     *
     * @param userId
     * @param orderId
     * @return
     */
    @Override
    public int cancelOrder(Long userId, Long orderId) {
        DrugOrder order = orderMapper.getOrderStatus(orderId);
        // 订单不存在
        if (order == null){
            return -1;
        }

        // 订单已取消
        if (order.getOrderStatus().intValue() == OrderStatus.CANCEL.get()){
            return -2;
        }

        // 订单不能取消
        if (order.getOrderStatus().intValue() != OrderStatus.INIT.get()){
            return -3;
        }

        // 不是userId拥有的订单
        if (order.getUserId() != userId){
            return -4;
        }

        return orderMapper.changeOrderStatus(orderId, OrderStatus.CANCEL.get());
    }

    /**
     * 确认下单
     *
     * @param userId
     * @param orderId
     * @param sellerId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultCode confirmOrder(Long userId, Long orderId, Long sellerId) {
        DrugOrder o = orderMapper.getOrderStatus(orderId);
        if (o == null){
            return ResultCode.ORDER_NOT_EXIST;
        }
        if (o.getOrderStatus().intValue() != OrderStatus.INIT.get()){
            return ResultCode.ORDER_INVALID_STATUS;
        }
        if (o.getUserId().longValue() != userId.longValue()){
            return ResultCode.ORDER_UNAUTHORIZED;
        }

        DrugSellerOrder sellerOrder = sellerOrderMapper.getSellerOrder(orderId, sellerId);
        if (sellerOrder == null){
            return ResultCode.ORDER_SELLER_NOT_EXIST;
        }

        // 确认药品单价
        List<DrugSellerOrderGoods> sogs = sellerOrderGoodsMapper.getByOrderAndSeller(orderId, sellerId);
        List<DrugOrderGoods> goods = new ArrayList<>();
        BigDecimal amount = BigDecimal.valueOf(0.0);
        for (DrugSellerOrderGoods sog : sogs){
            DrugOrderGoods g = new DrugOrderGoods();
            g.setId(sog.getOrderDrugId());
            g.setUnitPrice(sog.getUnitPrice());

            amount = amount.add(sog.getUnitPrice().multiply(new BigDecimal(sog.getQuantity())));
            goods.add(g);
        }
        int rows = drugOrderGoodsMapper.confirm(goods);
        if (rows < 1){
            return ResultCode.FAILED;
        }

        // 确认订单卖家(药师),订单状态，订单确认时间
        DrugSeller seller = sellerMapper.get(sellerId);
        DrugOrder order = new DrugOrder();
        order.setId(orderId);
        order.setOrderStatus(OrderStatus.CONFIRM.get());
        order.setConfirmTime(LocalDateTime.now());
        order.setSellerId(sellerId);
        order.setSellerName(seller.getName());
        order.setSellerStoreId(seller.getStoreId());
        order.setSellerStoreName(seller.getStoreName());
        order.setOrderAmount(amount);
        rows = orderMapper.confirm(order);
        if (rows < 1){
            return ResultCode.FAILED;
        }

        rows = sellerOrderMapper.changeStatusConfirm(orderId, sellerId);
        if (rows < 1){
            return ResultCode.FAILED;
        }
        return ResultCode.OK;
    }

    @Override
    public List<DrugOrder> list(Map<String, Object> conditionMap) {
        return orderMapper.list(conditionMap);
    }

    @Override
    public DrugOrder orderDetail(Long orderId) {
        return orderMapper.orderDetail(orderId);
    }

    @Override
    public List<DrugSellerOrder> listByOrder(Map<String, Object> conditionMap) {
        return sellerOrderMapper.listByOrder(conditionMap);
    }

    @Override
    public DrugOrder getOrderStatus(Long orderId) {
        return orderMapper.getOrderStatus(orderId);
    }

    @Override
    public int addRewardAmount(Long orderId, BigDecimal amount) {
        return orderMapper.addRewardAmount(orderId, amount);
    }

    @Override
    public int insertOrderShare(DrugOrderShare share) {
        return orderMapper.insertOrderShare(share);
    }

    @Override
    public List<Map<String, Object>> getOrderShare(Long orderId) {
        return orderMapper.getOrderShare(orderId);
    }
}
