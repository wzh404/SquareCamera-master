package com.wannengyongyao.drug.dao;

import com.wannengyongyao.drug.model.DrugUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DrugUserMapper {
    int insert(DrugUser record);

    DrugUser get(@Param("id")Long uid);
}