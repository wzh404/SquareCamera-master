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
}