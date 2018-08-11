package com.wannengyongyao.drug.dao;

import com.wannengyongyao.drug.model.DrugWeixinUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DrugUserWeixinMapper {
    int insert(DrugWeixinUser record);

    DrugWeixinUser getByOpenId(@Param("openId") String openId);
}