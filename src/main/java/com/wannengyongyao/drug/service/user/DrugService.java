package com.wannengyongyao.drug.service.user;

import com.github.pagehelper.Page;
import com.wannengyongyao.drug.model.Drug;
import com.wannengyongyao.drug.model.DrugRank;

public interface DrugService {
    /**
     * 根据名称搜索药品
     *
     * @param name
     * @return
     */
    Page<Drug> search(int page, int pageSize, String name);

    /**
     * 关注度排行
     *
     * @return
     */
    Page<DrugRank> listDrugHotRank(int page, int pageSize);

    /**
     * 稀少度排行
     *
     * @return
     */
    Page<DrugRank> listDrugRareRank(int page, int pageSize);

    /**
     * 成功率排行
     *
     * @return
     */
    Page<DrugRank> listDrugSuccessRank(int page, int pageSize);
}
