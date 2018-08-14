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
     * @param id
     * @return
     */
    int deleteUserCart(@Param("id")Integer id);
}