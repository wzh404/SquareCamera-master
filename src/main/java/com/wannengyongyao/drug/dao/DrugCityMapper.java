package com.wannengyongyao.drug.dao;

import com.wannengyongyao.drug.model.DrugBanner;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DrugCityMapper {
    List<DrugBanner> banner(@Param("classify")Integer classify);
}