package com.wannengyongyao.drug.dao;

import com.wannengyongyao.drug.model.DrugUserCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DrugUserCartMapper {
    /**
     *
     * @param record
     * @return
     */
    int insert(DrugUserCart record);

    /**
     *
     * @param userId
     * @return
     */
    List<DrugUserCart> listUserCart(@Param("userId")Long userId);

    /**
     *
     * @param carts
     * @return
     */
    int deleteUserCart(List<DrugUserCart> carts);

    /**
     *
     * @param id
     * @return
     */
    DrugUserCart get(@Param("id") Integer id);

    /**
     *
     * @param userId
     * @param drugId
     * @return
     */
    DrugUserCart getUserCart(@Param("userId")Long userId, @Param("drugId")Integer drugId);

    /**
     * 增加用户购物车药品数量
     *
     * @param userId
     * @param drugId
     * @param quantity
     * @return
     */
    int changeUserCartQuantity(@Param("userId")Long userId, @Param("drugId")Integer drugId, @Param("quantity")Integer quantity);
}