package com.wannengyongyao.drug.service.pharmacist;

import com.github.pagehelper.Page;
import com.wannengyongyao.drug.model.DrugStore;

import java.util.Map;

public interface StoreService {
    Page<DrugStore> list(int page, int pageSize, Map<String, Object> conditionMap);
}
