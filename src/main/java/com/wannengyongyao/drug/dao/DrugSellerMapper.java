package com.wannengyongyao.drug.dao;

import com.github.pagehelper.Page;
import com.wannengyongyao.drug.model.DrugSeller;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface DrugSellerMapper {
    /**
     * 用户结交的药师
     *
     * @param conditionMap
     * @return
     */
    Page<DrugSeller> myPharmacists(Map<String, Object> conditionMap);

    /**
     * 根据手机号获取卖家信息
     *
     * @param mobile
     * @return
     */
    DrugSeller getPharmacitsByMobile(@Param("mobile")String mobile);

    /**
     * 注册卖家
     * @param seller
     * @return
     */
    int insert(DrugSeller seller);

    /**
     * 更新卖家信息
     *
     * @param seller
     * @return
     */
    int update(DrugSeller seller);

    /**
     * 根据id获取卖家信息
     *
     * @param id
     * @return
     */
    DrugSeller get(@Param("id")Long id);

    /**
     *
     * @param openid
     * @return
     */
    DrugSeller getSellerByOpenid(@Param("openid")String openid);
}