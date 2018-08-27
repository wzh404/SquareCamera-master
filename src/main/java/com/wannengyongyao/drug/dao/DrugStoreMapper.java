package com.wannengyongyao.drug.dao;

import com.github.pagehelper.Page;
import com.wannengyongyao.drug.model.Drug;
import com.wannengyongyao.drug.model.DrugStore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DrugStoreMapper {
    int insert(DrugStore record);

    DrugStore get(@Param("id")Integer id);

    List<DrugStore> nearby(@Param("lon")Double lon, @Param("lat")Double lat, @Param("name")String name, @Param("distance")Integer distance);

    Page<DrugStore> list(Map<String, Object> conditionMap);
}