package com.wannengyongyao.drug.service;

import com.wannengyongyao.drug.vo.OrderVo;

public interface DrugOrderService {
    /**
     * 用户下单
     *
     * @param orderVo
     * @return
     */
    public Integer newOrder(OrderVo orderVo);
}
