package com.wannengyongyao.drug.dao;

import com.wannengyongyao.drug.model.DrugBanner;
import com.wannengyongyao.drug.model.DrugCity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DrugCityMapper {
    List<DrugBanner> banner(@Param("classify")Integer classify);

    List<DrugCity> getDistrict(@Param("code")String code);

    List<DrugCity> getCity(@Param("code")String code);

    List<DrugCity> getProvince();

    /**
     * 获取字典
     *
     * @param classify
     * @return
     */
    List<DrugCity> listDict(@Param("classify")String classify);
}