package com.wannengyongyao.drug.service.user;

import com.wannengyongyao.drug.model.DrugBanner;

import java.util.List;

public interface DrugCommonService {
    List<DrugBanner> banner(Integer classify);
}
