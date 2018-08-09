package com.wannengyongyao.drug.service.impl;

import com.wannengyongyao.drug.dao.DrugMapper;
import com.wannengyongyao.drug.dao.DrugOrderMapper;
import com.wannengyongyao.drug.dao.DrugUserMapper;
import com.wannengyongyao.drug.model.DrugOrder;
import com.wannengyongyao.drug.model.DrugOrderGoods;
import com.wannengyongyao.drug.model.DrugUser;
import com.wannengyongyao.drug.service.DrugOrderService;
import com.wannengyongyao.drug.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DrugOrderServiceImpl implements DrugOrderService {
    @Autowired
    private DrugUserMapper userMapper;

    @Autowired
    private DrugOrderMapper orderMapper;

    @Autowired
    private DrugMapper drugMapper;

    @Override
    public Integer newOrder(OrderVo orderVo) {
        DrugUser user = userMapper.get(orderVo.getUserId());
        DrugOrder order = orderVo.asOrder();
        order.setUserName(user.getName());

        List<DrugOrderGoods> goods = orderVo.asGoods();

        // int rows = orderMapper.insert(order);
        return null;
    }
}
