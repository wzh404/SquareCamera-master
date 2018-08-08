package com.wannengyongyao.drug.dao;

import com.wannengyongyao.drug.model.DrugStore;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DrugStoreMapper {
    int insert(DrugStore record);
}