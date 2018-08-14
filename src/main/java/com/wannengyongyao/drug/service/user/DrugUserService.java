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

    /**
     * 新增购物车
     *
     * @param record
     * @return
     */
    int insert(DrugUserCart record);

    /**
     * 查询购物车
     *
     * @param userId
     * @return
     */
    List<DrugUserCart> listUserCart(Long userId);

    /**
     * 删除购物车
     *
     * @param carts
     * @return
     */
    int deleteUserCart(List<DrugUserCart> carts);

    /**
     * 用户长期用药列表
     *
     * @param userId
     * @return
     */
    List<DrugUserLongterm> listUserLongTerm(Long userId);

    /**
     * 新增用户长期用药
     *
     * @param longterm
     * @return
     */
    int insertUserLongTerm(DrugUserLongterm longterm);

    /**
     * 删除用户长期用药
     *
     * @return
     */
    int deleteUserLongTerm(List<DrugUserLongterm> longterms);
}
