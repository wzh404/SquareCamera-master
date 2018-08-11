package com.wannengyongyao.drug.dao;

import com.wannengyongyao.drug.model.DrugStore;
import com.wannengyongyao.drug.model.DrugUser;
import com.wannengyongyao.drug.model.DrugUserPharmacist;
import com.wannengyongyao.drug.model.DrugUserStore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DrugUserMapper {
    int insert(DrugUser record);

    DrugUser get(@Param("id")Long uid);

    /**
     * 结交药师
     *
     * @param pharmacist
     * @return
     */
    int insertUserPharmacist(DrugUserPharmacist pharmacist);

    /**
     * 药师是否已结交
     *
     * @param userId
     * @param pharmacistId
     * @return
     */
    int getUserPharmacist(@Param("userId")Long userId, @Param("pharmacistId")Integer pharmacistId);

    /**
     * 保存代收点
     *
     * @param store
     * @return
     */
    int insertUserStore(DrugUserStore store);

    /**
     * 药店是否是我的代收药店
     *
     * @param userId
     * @param storeId
     * @return >0 是， 其它：否
     */
    int getUserStoreCount(@Param("userId")Long userId, @Param("storeId")Integer storeId);

    /**
     * 我的代收药店
     *
     * @param userId
     * @return
     */
    List<DrugStore> getUserStores(@Param("userId")Long userId);

    DrugUser getByMobile(@Param("mobile")String mobile);
}