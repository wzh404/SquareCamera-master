package com.wannengyongyao.drug.dao;

import com.wannengyongyao.drug.model.DrugSellerOrderGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DrugSellerOrderGoodsMapper {
    int insert(List<DrugSellerOrderGoods> orderGoods);

    /**
     *
     * @param orderId
     * @param sellerId
     * @return
     */
    List<DrugSellerOrderGoods> getByOrderAndSeller(@Param("orderId")Long orderId, @Param("sellerId")Long sellerId);

    /**
     *
     * @param orderId
     * @return
     */
    String getFirstDrugName(@Param("orderId")Long orderId);
}