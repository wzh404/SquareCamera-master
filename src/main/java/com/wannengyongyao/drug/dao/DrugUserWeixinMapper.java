package com.wannengyongyao.drug.dao;

import com.wannengyongyao.drug.model.DrugWeixinUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DrugUserWeixinMapper {
    /**
     * 保存用户微信授权信息
     *
     * @param record
     * @return
     */
    int insert(DrugWeixinUser record);

    /**
     * 根据openid获取用户微信授权信息
     * @param openId
     * @return
     */
    DrugWeixinUser getByOpenId(@Param("openId") String openId);
}