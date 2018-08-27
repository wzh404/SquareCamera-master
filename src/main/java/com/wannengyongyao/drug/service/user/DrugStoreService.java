package com.wannengyongyao.drug.service.user;

import com.wannengyongyao.drug.model.DrugStore;

import java.util.List;

public interface DrugStoreService {
    /**
     * 查找附近药店
     *
     * @param lon
     * @param lat
     * @param distance
     * @return
     */
    List<DrugStore> nearby(Double lon, Double lat, String name, Integer distance);
}
