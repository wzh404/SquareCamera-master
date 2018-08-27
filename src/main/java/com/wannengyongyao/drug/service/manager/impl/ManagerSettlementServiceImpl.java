package com.wannengyongyao.drug.service.manager.impl;

import com.wannengyongyao.drug.common.status.BalanceClassify;
import com.wannengyongyao.drug.common.status.OrderStatus;
import com.wannengyongyao.drug.dao.*;
import com.wannengyongyao.drug.model.*;
import com.wannengyongyao.drug.service.manager.ManagerSettlementService;
import com.wannengyongyao.drug.service.user.DrugUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service("managerSettlementService")
public class ManagerSettlementServiceImpl implements ManagerSettlementService {
    private final Logger logger = LoggerFactory.getLogger(ManagerSettlementServiceImpl.class);

    @Autowired
    private DrugOrderMapper orderMapper;

    @Autowired
    private DrugOrderGoodsMapper goodsMapper;

    @Autowired
    private DrugSellerBalanceMapper balanceMapper;

    @Autowired
    private DrugSellerMapper sellerMapper;

    @Autowired
    private DrugSellerOrderGoodsMapper sellerGoodsMapper;

    @Autowired
    private DrugUserService userService;

    /**
     * 订单成功后结算
     *
     * @param orderId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int settle(Long orderId) {
        DrugOrder order = orderMapper.getSettleOrder(orderId);
        if (order == null){
            logger.error("order {} not exists", orderId);
            return -1;
        }
        if (order.getPayStatus() != 1){
            logger.error("order {} not payment", orderId);
            return -2;
        }

        if (order.getOrderStatus() != OrderStatus.COMPLETED.get()){
            logger.error("order {} not completed", orderId);
            return -3;
        }

        long minutes = order.getSignTime().until(LocalDateTime.now(), ChronoUnit.MINUTES);
        if (minutes < 1440){
            logger.error("order {} settle date has not arrived", orderId);
            return -4;
        }

        if (order.getSettlementStatus().intValue() == 1){
            logger.info("order {} settle already.", orderId);
            return -5;
        }

        // 服务费分成
        BigDecimal sellerServiceAmount = order.getServiceCharge()
                .multiply(BigDecimal.valueOf(0.4))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal storeServiceAmount = order.getServiceCharge()
                .multiply(BigDecimal.valueOf(0.3))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal systemServiceAmount = order.getServiceCharge()
                .subtract(sellerServiceAmount)
                .subtract(storeServiceAmount);

        // 卖家收入流水
        List<DrugSellerBalance> sellerBalanceList = new ArrayList<>();
        sellerBalanceList.add(drug(order));
        sellerBalanceList.add(service(order, sellerServiceAmount));
        sellerBalanceList.add(reward(order));
        balanceMapper.sellerBalance(sellerBalanceList);

        // 卖家账户
        BigDecimal sellerAmount = order.getOrderAmount()
                .add(order.getRewardAmount())
                .add(sellerServiceAmount);
        balanceMapper.sellerAccount(order.getSellerId(), sellerAmount);

        // 平台收入流水
        List<DrugSystemBalance> systemBalanceList = new ArrayList<>();
        systemBalanceList.add(freight(order));
        systemBalanceList.add(sservice(order, systemServiceAmount));
        // 平台支出流水
        systemBalanceList.add(coupon(order));
        balanceMapper.systemBalance(systemBalanceList);

        // 平台账户
        BigDecimal income = systemServiceAmount.add(order.getFreight());
        BigDecimal expenditure = order.getDiscountAmount();
        balanceMapper.systemAccount(expenditure, income);

        // 代收药店


        // 药师成功抢单数增加
        String drugName = sellerGoodsMapper.getFirstDrugName(orderId);
        sellerMapper.increaseSuccess(order.getSellerId(), drugName);

        // 更新订单结算状态
        orderMapper.changeOrderSettlementStatus(orderId);

        // 长期用药
        DrugUserLongterm longTerm = new DrugUserLongterm();
        longTerm.setUserId(order.getUserId());
        List<Integer> ids = goodsMapper.listDrugs(orderId);
        for (Integer id : ids) {
            longTerm.setDrugId(id);
            longTerm.setCreateTime(LocalDateTime.now());
            userService.insertUserLongTerm(longTerm);
        }
        return 0;
    }

    @Override
    public List<DrugOrder> listUnSettlementOrder() {
        return orderMapper.listUnSettlementOrder();
    }

    /**
     * 药费
     *
     * @param order
     * @return
     */
    private DrugSellerBalance drug(DrugOrder order){
        DrugSellerBalance balance = new DrugSellerBalance();
        balance.setOrderId(order.getId());
        balance.setSellerId(order.getSellerId());
        balance.setDebit(1);
        balance.setCreateTime(LocalDateTime.now());

        balance.setAmount(order.getOrderAmount());
        balance.setClassify(BalanceClassify.DRUG.get());
        balance.setRemark("药费");
        return balance;
    }

    /**
     * 卖家服务费
     *
     * @param order
     * @param fee
     * @return
     */
    private DrugSellerBalance service(DrugOrder order, BigDecimal fee){
        DrugSellerBalance balance = new DrugSellerBalance();
        balance.setOrderId(order.getId());
        balance.setSellerId(order.getSellerId());
        balance.setDebit(1);
        balance.setCreateTime(LocalDateTime.now());

        balance.setAmount(fee);
        balance.setClassify(BalanceClassify.SERVICE.get());
        balance.setRemark("服务费");

        return balance;
    }

    /**
     * 悬赏费
     *
     * @param order
     * @return
     */
    private DrugSellerBalance reward(DrugOrder order){
        DrugSellerBalance balance = new DrugSellerBalance();
        balance.setOrderId(order.getId());
        balance.setSellerId(order.getSellerId());
        balance.setDebit(1);
        balance.setCreateTime(LocalDateTime.now());

        balance.setAmount(order.getRewardAmount());
        balance.setClassify(BalanceClassify.REWARD.get());
        balance.setRemark("悬赏费");

        return balance;
    }

    /**
     * 平台运费
     *
     * @param order
     * @return
     */
    private DrugSystemBalance freight(DrugOrder order){
        DrugSystemBalance balance = new DrugSystemBalance();
        balance.setOrderId(order.getId());
        balance.setDebit(1);
        balance.setCreateTime(LocalDateTime.now());

        balance.setAmount(order.getFreight());
        balance.setClassify(BalanceClassify.SHIPPED.get());
        balance.setRemark("运费");
        return balance;
    }

    /**
     * 平台服务费
     *
     * @param order
     * @return
     */
    private DrugSystemBalance sservice(DrugOrder order, BigDecimal fee){
        DrugSystemBalance balance = new DrugSystemBalance();
        balance.setOrderId(order.getId());
        balance.setDebit(1);
        balance.setCreateTime(LocalDateTime.now());

        balance.setAmount(fee);
        balance.setClassify(BalanceClassify.SERVICE.get());
        balance.setRemark("服务费提成");
        return balance;
    }

    /**
     * 平台支出 - 优惠券
     *
     * @param order
     * @return
     */
    private DrugSystemBalance coupon(DrugOrder order){
        DrugSystemBalance balance = new DrugSystemBalance();
        balance.setOrderId(order.getId());
        balance.setDebit(-1);
        balance.setCreateTime(LocalDateTime.now());

        balance.setAmount(order.getDiscountAmount());
        balance.setClassify(BalanceClassify.COUPON.get());
        balance.setRemark("优惠券金额");
        return balance;
    }
}
