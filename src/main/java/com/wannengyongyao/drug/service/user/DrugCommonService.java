package com.wannengyongyao.drug.service.user;

import com.wannengyongyao.drug.model.DrugBanner;
import com.wannengyongyao.drug.model.DrugCity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DrugCommonService {
    List<DrugBanner> banner(Integer classify);

    List<DrugCity> getDistrict(String code);

    List<DrugCity> getCity(String code);

    List<DrugCity> getProvince();

    List<DrugCity> listDict(String classify);

    /**
     * 已支付
     *
     * @param orderId
     * @return
     */
    int payment(Long orderId);
}
