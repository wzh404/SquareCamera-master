package com.wannengyongyao.drug.service.user.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wannengyongyao.drug.dao.DrugMapper;
import com.wannengyongyao.drug.model.Drug;
import com.wannengyongyao.drug.service.user.DrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("drugService")
public class DrugServiceImpl implements DrugService {
    @Autowired
    private DrugMapper drugMapper;

    @Override
    public Page<Drug> search(int page, int pageSize, String name) {
        PageHelper.startPage(page, pageSize);
        return drugMapper.search(name);
    }
}
