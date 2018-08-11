package com.wannengyongyao.drug.service.user;

import com.github.pagehelper.Page;
import com.wannengyongyao.drug.model.Drug;

public interface DrugService {
    /**
     * 根据名称搜索药品
     *
     * @param name
     * @return
     */
    Page<Drug> search(int page, int pageSize, String name);
}
