package com.wannengyongyao.drug.dao;


import com.wannengyongyao.drug.model.DrugSellerBalance;
import com.wannengyongyao.drug.model.DrugSystemBalance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface DrugSellerBalanceMapper {
    /**
     * 用户账户流水
     *
     * @param balances
     * @return
     */
    int sellerBalance(List<DrugSellerBalance> balances);

    /**
     * 卖家账户更新
     *
     * @param sellerId
     * @param amount
     * @return
     */
    int sellerAccount(@Param("sellerId")Long sellerId, @Param("amount")BigDecimal amount);

    /**
     * 平台账户流水
     *
     * @param balances
     * @return
     */
    int systemBalance(List<DrugSystemBalance> balances);

    /**
     * 平台账户更新
     *
     * @param expenditure 支出
     * @param income 收入
     * @return
     */
    int systemAccount(@Param("expenditure")BigDecimal expenditure, @Param("income")BigDecimal income);
}