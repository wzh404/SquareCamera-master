package com.wannengyongyao.drug.dao;

import com.wannengyongyao.drug.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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

    /**
     * 根据手机号查询用户信息
     *
     * @param mobile
     * @return
     */
    DrugUser getByMobile(@Param("mobile")String mobile);

    /**
     * 根据openid查询用户
     *
     * @param openid
     * @return
     */
    DrugUser getUserByOpenid(@Param("openid")String openid);

    /**
     * 药师结交的用户列表
     *
     * @param conditionMap
     * @return
     */
    List<DrugUser> getPharmacistUsers(Map<String, Object> conditionMap);

    /**
     * 用户长期用药列表
     *
     * @param userId
     * @return
     */
    List<DrugUserLongterm> listUserLongTerm(@Param("userId")Long userId);

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

    /**
     *
     * @param userId
     * @param drugId
     * @return
     */
    DrugUserLongterm getUserLongTerm(@Param("userId")Long userId, @Param("drugId")Integer drugId);

    /**
     * 查看历史订单用户订单中指定药品的数量
     *
     * @param userId
     * @param drugId
     * @return
     */
    int getUserOrderDrugCount(@Param("userId")Long userId, @Param("drugId")Integer drugId);
}