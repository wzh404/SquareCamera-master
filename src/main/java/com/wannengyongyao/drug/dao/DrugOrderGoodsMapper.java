package com.wannengyongyao.drug.dao;

import com.wannengyongyao.drug.model.DrugOrderGoods;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DrugOrderGoodsMapper {
    int insert(List<DrugOrderGoods> goods);
}