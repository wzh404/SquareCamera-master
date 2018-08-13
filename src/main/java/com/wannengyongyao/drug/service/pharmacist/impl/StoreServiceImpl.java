package com.wannengyongyao.drug.service.pharmacist.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wannengyongyao.drug.dao.DrugStoreMapper;
import com.wannengyongyao.drug.model.DrugStore;
import com.wannengyongyao.drug.service.pharmacist.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("storeService")
public class StoreServiceImpl implements StoreService {
    @Autowired
    private DrugStoreMapper storeMapper;

    @Override
    public Page<DrugStore> list(int page, int pageSize, Map<String, Object> conditionMap) {
        PageHelper.startPage(page, pageSize);
        return storeMapper.list(conditionMap);
    }
}
