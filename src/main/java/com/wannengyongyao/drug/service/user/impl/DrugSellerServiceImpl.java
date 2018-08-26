package com.wannengyongyao.drug.service.user.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wannengyongyao.drug.dao.DrugSellerMapper;
import com.wannengyongyao.drug.model.DrugSeller;
import com.wannengyongyao.drug.service.user.DrugSellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("drugSellerService")
public class DrugSellerServiceImpl implements DrugSellerService {
    @Autowired
    private DrugSellerMapper sellerMapper;

    @Override
    public Page<DrugSeller> myPharmacists(int page, int pageSize, Map<String, Object> conditionMap) {
        PageHelper.startPage(page, pageSize);
        return sellerMapper.myPharmacists(conditionMap);
    }

    @Override
    public Page<DrugSeller> reliableSeller(int page, int pageSize, long userId) {
        PageHelper.startPage(page, pageSize);
        if (userId < 1L) {
            return sellerMapper.reliableSeller();
        }

        return sellerMapper.reliableUserSeller(userId);
    }
}
