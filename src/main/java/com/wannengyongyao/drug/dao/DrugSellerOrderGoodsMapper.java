package com.wannengyongyao.drug.dao;

import com.wannengyongyao.drug.model.DrugSellerOrderGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DrugSellerOrderGoodsMapper {
    int insert(List<DrugSellerOrderGoods> orderGoods);

    List<DrugSellerOrderGoods> getByOrderAndSeller(@Param("orderId")Long orderId, @Param("sellerId")Long sellerId);
}