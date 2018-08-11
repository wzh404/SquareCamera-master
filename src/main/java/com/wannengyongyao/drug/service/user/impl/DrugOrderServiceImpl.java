package com.wannengyongyao.drug.service.user.impl;

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
import java.time.LocalDateTime;
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

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Integer newOrder(DrugOrderVo orderVo) {
        // 订单用户信息
        DrugUser user = userMapper.get(orderVo.getUserId());
        DrugOrder order = orderVo.asOrder();
        order.setUserName(user.getName());

        // 订单代收药店
        DrugStore store = drugStoreMapper.get(orderVo.getStoreId());
        order.setCollectionStore(store.getName());
        long orderId = StringUtil.getOrderId(orderVo.getUserId());
        order.setId(orderId);


        // 订单药品信息
        List<DrugOrderGoods> goods = orderVo.asGoods();
        for (DrugOrderGoods g : goods){
            Drug drug = drugMapper.get(g.getDrugId());
            g.setDrugName(drug.getName());
            g.setManufacturer(drug.getManufacturer());
            g.setSpecifications(drug.getSpecifications());
            g.setUnitPrice(new BigDecimal(0.0));
            g.setOrderId(orderId);
            g.setCreateTime(LocalDateTime.now());
        }
        int rows = drugOrderGoodsMapper.insert(goods);

        // 我的代收药店
        DrugUserStore userStore = new DrugUserStore();
        userStore.setUserId(orderVo.getUserId());
        userStore.setStoreId(orderVo.getStoreId());
        userStore.setCreateTime(LocalDateTime.now());
        userMapper.insertUserStore(userStore);

        rows = orderMapper.insert(order);
        return rows;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Integer newPhotoOrder(PhotoOrderVo orderVo) {
        // 订单用户信息
        DrugUser user = userMapper.get(orderVo.getUserId());
        DrugOrder order = orderVo.asOrder();
        order.setUserName(user.getName());

        // 订单代收药店
        DrugStore store = drugStoreMapper.get(orderVo.getStoreId());
        order.setCollectionStore(store.getName());
        long orderId = StringUtil.getOrderId(orderVo.getUserId());
        order.setId(orderId);

        // 我的代收药店
        DrugUserStore userStore = new DrugUserStore();
        userStore.setUserId(orderVo.getUserId());
        userStore.setStoreId(orderVo.getStoreId());
        userStore.setCreateTime(LocalDateTime.now());
        userMapper.insertUserStore(userStore);

        // 订单拍照药品信息
        List<DrugOrderGoods> goods = orderVo.asGoods();
        for (DrugOrderGoods g : goods){
            g.setOrderId(orderId);
        }
        drugOrderGoodsMapper.insert(goods);

        int rows = orderMapper.insert(order);
        return rows;
    }

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
            return -3;
        }

        return orderMapper.changeOrderStatus(orderId, OrderStatus.CANCEL.get());
    }

    @Override
    public List<DrugOrder> list(Map<String, Object> conditionMap) {
        return orderMapper.list(conditionMap);
    }

    @Override
    public DrugOrder orderDetail(Long orderId) {
        return orderMapper.orderDetail(orderId);
    }
}
