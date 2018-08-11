package com.wannengyongyao.drug.service.user;

import com.wannengyongyao.drug.model.*;

import java.util.List;

public interface DrugUserService {
    /**
     * 结交药师
     *
     * @param pharmacist
     * @return
     */
    int insertUserPharmacist(DrugUserPharmacist pharmacist);

    /**
     * 保存代收点
     *
     * @param store
     * @return
     */
    int insertUserStore(DrugUserStore store);

    /**
     * 我的代收药店
     *
     * @param userId
     * @return
     */
    List<DrugStore> getUserStores(Long userId);

    int insertWeixinUser(DrugWeixinUser user);

    int insertUser(DrugUser user);
}
