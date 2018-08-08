package com.wannengyongyao.drug.dao;

import com.wannengyongyao.drug.model.DrugUserCart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DrugUserCartMapper {
    int insert(DrugUserCart record);
}