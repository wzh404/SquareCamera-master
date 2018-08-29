package com.wannengyongyao.drug.service.user.impl;

import com.wannengyongyao.drug.dao.DrugCityMapper;
import com.wannengyongyao.drug.dao.DrugOrderMapper;
import com.wannengyongyao.drug.model.DrugBanner;
import com.wannengyongyao.drug.model.DrugCity;
import com.wannengyongyao.drug.service.user.DrugCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("drugCommonService")
public class DrugCommonServiceImpl implements DrugCommonService {
    @Autowired
    private DrugCityMapper cityMapper;

    @Autowired
    private DrugOrderMapper orderMapper;

    @Override
    public List<DrugBanner> banner(Integer classify) {
        return cityMapper.banner(classify);
    }

    @Override
    public List<DrugCity> getDistrict(String code) {
        return cityMapper.getDistrict(code);
    }

    @Override
    public List<DrugCity> getCity(String code) {
        return cityMapper.getCity(code);
    }

    @Override
    public List<DrugCity> getProvince() {
        return cityMapper.getProvince();
    }

    @Override
    public List<DrugCity> listDict(String classify) {
        return cityMapper.listDict(classify);
    }

    @Override
    public int payment(Long orderId) {
        return orderMapper.payment(orderId);
    }
}
