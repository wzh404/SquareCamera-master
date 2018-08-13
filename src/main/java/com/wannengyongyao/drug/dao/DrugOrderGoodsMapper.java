package com.wannengyongyao.drug.dao;

import com.wannengyongyao.drug.model.DrugOrderGoods;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DrugOrderGoodsMapper {
    /**
     * 插入订单药品详情
     *
     * @param goods
     * @return
     */
    int insert(List<DrugOrderGoods> goods);

    /**
     * 订单确认
     *
     * @param goods
     * @return
     */
    int confirm(List<DrugOrderGoods> goods);
}