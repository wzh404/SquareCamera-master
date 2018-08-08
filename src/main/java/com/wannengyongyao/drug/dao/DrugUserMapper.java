package com.wannengyongyao.drug.dao;

import com.wannengyongyao.drug.model.DrugUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DrugUserMapper {
    int insert(DrugUser record);
}