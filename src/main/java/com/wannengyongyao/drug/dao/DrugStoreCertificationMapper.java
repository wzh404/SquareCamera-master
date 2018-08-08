package com.wannengyongyao.drug.dao;

import com.wannengyongyao.drug.model.DrugStoreCertification;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DrugStoreCertificationMapper {

    int insert(DrugStoreCertification record);
}