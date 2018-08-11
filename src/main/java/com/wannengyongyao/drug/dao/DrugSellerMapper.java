package com.wannengyongyao.drug.dao;

import com.github.pagehelper.Page;
import com.wannengyongyao.drug.model.DrugSeller;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface DrugSellerMapper {

    Page<DrugSeller> myPharmacists(Map<String, Object> conditionMap);
}