package com.wannengyongyao.drug.service.pharmacist.impl;

import com.wannengyongyao.drug.common.ResultCode;
import com.wannengyongyao.drug.common.ResultObject;
import com.wannengyongyao.drug.common.status.OrderStatus;
import com.wannengyongyao.drug.dao.*;
import com.wannengyongyao.drug.model.*;
import com.wannengyongyao.drug.service.pharmacist.PharmacistService;
import com.wannengyongyao.drug.vo.PharmacistOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("pharmacistService")
public class PharmacistServiceImpl implements PharmacistService {
    @Autowired
    private DrugSellerMapper sellerMapper;

    @Autowired
    private DrugOrderMapper orderMapper;

    @Autowired
    private DrugSellerOrderGoodsMapper sellerOrderGoodsMapper;

    @Autowired
    private DrugSellerOrderMapper sellerOrderMapper;

    @Autowired
    private DrugUserMapper userMapper;

    @Autowired
    private DrugSellerBalanceMapper balanceMapper;

    @Override
    public DrugSeller getPharmacitsByMobile(String mobile) {
        return sellerMapper.getPharmacitsByMobile(mobile);
    }

    @Override
    public DrugSeller getSeller(Long id) {
        DrugSeller seller = sellerMapper.get(id);
        int orderNum = orderMapper.getSellerOrderTotal(id);
        seller.setOrderNum(orderNum);
        return seller;
    }

    @Override
    public DrugOrder getOrderStatus(Long orderId) {
        return orderMapper.getOrderStatus(orderId);
    }

    @Override
    public int insert(DrugSeller seller) {
        return sellerMapper.insert(seller);
    }

    @Override
    public int update(DrugSeller seller) {
        return sellerMapper.update(seller);
    }

    /**
     * 发送报价
     *
     * @param orderVo
     * @param sellerId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int grab(PharmacistOrderVo orderVo, long sellerId) {
        DrugSellerOrder so = sellerOrderMapper.getSellerOrder(orderVo.getOrderId(), sellerId);
        if (so != null) {
            return -1;
        }
        DrugSeller seller = sellerMapper.get(sellerId);
        if (seller == null) {
            return -2;
        }
        DrugOrder order = orderMapper.orderDetail(orderVo.getOrderId());
        if (order == null) {
            return -3;
        }

        // 药师抢单
        DrugSellerOrder sellerOrder = orderVo.asSellerOrder();
        sellerOrder.setCreateTime(LocalDateTime.now());
        sellerOrder.setUserId(order.getUserId());
        sellerOrder.setUserName(order.getUserName());
        sellerOrder.setSellerId(sellerId);
        sellerOrder.setSellerName(seller.getName());
        sellerOrder.setDrugStore(seller.getStoreName());
        sellerOrder.setDrugStoreId(seller.getStoreId());
        sellerOrder.setStatus(0);

        Map<Integer, DrugOrderGoods> drugMap = order.getDrugs().stream().collect(Collectors.toMap(d -> d.getId(), d -> d));
        // 药师抢单明细
        BigDecimal amount = BigDecimal.valueOf(0.0);
        List<DrugSellerOrderGoods> orderGoodsList = orderVo.asSellerOrderGoods();
        for (DrugSellerOrderGoods sog : orderGoodsList) {
            DrugOrderGoods og = drugMap.get(sog.getOrderDrugId().intValue());
            if (og != null) {
                sog.setOrderId(order.getId());
                sog.setDrugId(og.getDrugId());
                sog.setDrugName(og.getDrugName());
                sog.setManufacturer(og.getManufacturer());
                sog.setQuantity(og.getQuantity());
                sog.setSpecifications(og.getSpecifications());
                sog.setSellerId(sellerId);
                sog.setPhotos(og.getPhotos());
                sog.setRemark(og.getRemark());
                amount = amount.add(sog.getUnitPrice().multiply(new BigDecimal(sog.getQuantity())));
            } else {
                return -4;
            }
        }
        sellerOrder.setAmount(amount);
        sellerOrderMapper.insert(sellerOrder);
        // 增加药师抢购的订单数量
        sellerMapper.increaseGrab(sellerId);
        // 增加订单的抢购人数
        orderMapper.increaseSellerNum(order.getId());
        return sellerOrderGoodsMapper.insert(orderGoodsList);
    }

    @Override
    public List<DrugOrder> listSellerGrab(Map<String, Object> conditionMap) {
        return orderMapper.listSellerGrab(conditionMap);
    }

    @Override
    public List<DrugOrder> listSeller(Map<String, Object> conditionMap) {
        return orderMapper.listSeller(conditionMap);
    }

    @Override
    public List<DrugSellerOrder> listSellerUnconfirmed(Map<String, Object> conditionMap) {
        return sellerOrderMapper.listSellerUnconfirmed(conditionMap);
    }

    @Override
    public List<DrugOrder> listCollection(Map<String, Object> conditionMap) {
        return orderMapper.listCollection(conditionMap);
    }

    @Override
    public int sellerShipping(DrugOrder order) {
        return orderMapper.sellerShipping(order);
    }

    @Override
    public int sellerCollection(DrugOrder order) {
        return orderMapper.sellerCollection(order);
    }

    @Override
    public List<DrugUser> getPharmacistUsers(Map<String, Object> conditionMap) {
        return userMapper.getPharmacistUsers(conditionMap);
    }

    @Override
    public DrugSeller getSellerByOpenid(String openid) {
        return sellerMapper.getSellerByOpenid(openid);
    }

    @Override
    public Map<String, Object> myBalance(Long sellerId) {
        DrugSeller seller = sellerMapper.get(sellerId);
        if (seller == null){
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("account", seller.getAccountBalance());
        map.put("available", seller.getAvailableBalance());

        // 本月业绩
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("sellerId", sellerId);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime one = LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0,0,0);
        conditionMap.put("endTime", now);
        conditionMap.put("startTime", one);
        BigDecimal month = sellerMapper.sumSeller(conditionMap);
        map.put("month", month);

        // 平台奖励（服务费）收入
        conditionMap.clear();
        conditionMap.put("sellerId", sellerId);
        conditionMap.put("classify", "1002");
        BigDecimal service = sellerMapper.sumSeller(conditionMap);
        map.put("service", service);

        // 悬赏收入
        conditionMap.clear();
        conditionMap.put("sellerId", sellerId);
        conditionMap.put("classify", "1003");
        BigDecimal reward = sellerMapper.sumSeller(conditionMap);
        map.put("reward", reward);

        return map;
    }

    @Override
    public ResultCode collectionOrder(Long orderId, Long sellerId) {
        DrugSeller seller = sellerMapper.get(sellerId);
        if (seller == null){
            return ResultCode.PHARMACIST_NOT_EXIST;
        }
        // 订单是否存在
        DrugOrder order = orderMapper.getSettleOrder(orderId);
        if (order == null){
            return ResultCode.ORDER_NOT_EXIST;
        }
        // 是否已发货
        if (order.getOrderStatus().intValue() != OrderStatus.SHIPPED.get()){
            return ResultCode.BAD_REQUEST;
        }
        // 是否代收药店
        if (seller.getStoreId().intValue() != order.getCollectionStoreId().intValue()){
            return ResultCode.BAD_REQUEST;
        }

        int rows = orderMapper.collectionOrder(orderId);
        return rows > 0 ? ResultCode.OK : ResultCode.FAILED;
    }
}