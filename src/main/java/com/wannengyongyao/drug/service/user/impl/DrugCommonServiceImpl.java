package com.wannengyongyao.drug.service.user.impl;

import com.wannengyongyao.drug.dao.DrugCityMapper;
import com.wannengyongyao.drug.model.DrugBanner;
import com.wannengyongyao.drug.service.user.DrugCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("drugCommonService")
public class DrugCommonServiceImpl implements DrugCommonService {
    @Autowired
    private DrugCityMapper cityMapper;

    @Override
    public List<DrugBanner> banner(Integer classify) {
        return cityMapper.banner(classify);
    }
}
