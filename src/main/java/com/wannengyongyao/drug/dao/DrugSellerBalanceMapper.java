package com.wannengyongyao.drug.dao;


import com.wannengyongyao.drug.model.DrugSellerBalance;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DrugSellerBalanceMapper {
    /**
     * 用户收入
     *
     * @param balance
     * @return
     */
    int income(DrugSellerBalance balance);
}