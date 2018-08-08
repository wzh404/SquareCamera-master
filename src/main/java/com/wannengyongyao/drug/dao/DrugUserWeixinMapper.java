package com.wannengyongyao.drug.dao;

import com.wannengyongyao.drug.model.DrugUserWeixin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DrugUserWeixinMapper {
    int insert(DrugUserWeixin record);

}