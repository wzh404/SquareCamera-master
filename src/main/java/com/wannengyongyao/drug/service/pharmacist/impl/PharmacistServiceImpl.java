package com.wannengyongyao.drug.service.pharmacist.impl;

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
import java.util.List;
import java.util.Map;

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
        return sellerMapper.get(id);
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
        if (so != null){
            return -1;
        }
        DrugSeller seller = sellerMapper.get(sellerId);
        if (seller == null){
            return -2;
        }
        DrugOrder order = orderMapper.orderDetail(orderVo.getOrderId());
        if (order == null){
            return -3;
        }

        // 药师抢单
        DrugSellerOrder sellerOrder = orderVo.asSellerOrder();
        sellerOrder.setCreateTime(LocalDateTime.now());
        sellerOrder.setUserId(order.getUserId());
        sellerOrder.setUserName(order.getUserName());
        sellerOrder.setSellerId(seller.getId());
        sellerOrder.setSellerName(seller.getName());
        sellerOrder.setDrugStore(seller.getStoreName());
        sellerOrder.setDrugStoreId(seller.getStoreId());
        sellerOrder.setStatus(0);

        // 药师抢单明细
        BigDecimal amount = BigDecimal.valueOf(0.0);
        List<DrugSellerOrderGoods> orderGoodsList = orderVo.asSellerOrderGoods();
        for (DrugSellerOrderGoods sog : orderGoodsList){
            for (DrugOrderGoods og : order.getDrugs()){
                if (sog.getOrderDrugId().intValue() == og.getId().intValue()){
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
                }
            }
        }
        sellerOrder.setAmount(amount);
        sellerOrderMapper.insert(sellerOrder);
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
    public List<DrugUser> getPharmacistUsers(Long pharmacistId) {
        return userMapper.getPharmacistUsers(pharmacistId);
    }

    @Override
    public int income(Long sellerId, Long orderId, BigDecimal amount, String remark) {
        DrugSellerBalance balance = new DrugSellerBalance();
        balance.setAmount(amount);
        balance.setSellerId(sellerId);
        balance.setOrderId(orderId);
        balance.setCreateTime(LocalDateTime.now());
        balance.setRemark(remark);
        balance.setDebit(1);

        return balanceMapper.income(balance);
    }
}
