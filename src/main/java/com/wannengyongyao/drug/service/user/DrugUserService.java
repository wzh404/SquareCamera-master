package com.wannengyongyao.drug.service.user;

import com.wannengyongyao.drug.model.*;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 保存用户微信授权信息
     *
     * @param user
     * @return
     */
    int insertWeixinUser(DrugWeixinUser user);

    /**
     * 注册用户
     *
     * @param user
     * @return
     */
    int insertUser(DrugUser user);

    /**
     * 根据openid获取用户微信授权信息
     * @param openId
     * @return
     */
    DrugWeixinUser getByOpenId(String openId);
}
