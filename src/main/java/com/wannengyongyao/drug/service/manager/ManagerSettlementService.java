package com.wannengyongyao.drug.service.manager;

import com.wannengyongyao.drug.model.DrugOrder;

import java.util.List;

public interface ManagerSettlementService {
    int settle(Long orderId);

    /**
     * 查询未结算的订单
     *
     * @return
     */
    List<DrugOrder> listUnSettlementOrder();
}
