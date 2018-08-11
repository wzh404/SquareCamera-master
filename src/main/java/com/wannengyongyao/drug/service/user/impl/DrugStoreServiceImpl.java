package com.wannengyongyao.drug.service.user.impl;

import com.wannengyongyao.drug.dao.DrugStoreMapper;
import com.wannengyongyao.drug.model.DrugStore;
import com.wannengyongyao.drug.service.user.DrugStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("drugStoreService")
public class DrugStoreServiceImpl implements DrugStoreService {
    @Autowired
    private DrugStoreMapper storeMapper;

    @Override
    public List<DrugStore> nearby(Double lon, Double lat, Integer distance) {
        return storeMapper.nearby(lon, lat, distance);
    }
}
