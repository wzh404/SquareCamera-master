package com.wannengyongyao.drug.service.user;

import com.github.pagehelper.Page;
import com.wannengyongyao.drug.model.*;
import com.wannengyongyao.drug.vo.UserAddressVo;
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
     *
     * @param user
     * @return
     */
    Long insertWeixinAndUser(DrugWeixinUser user);


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

    /**
     * 我的优惠券
     *
     * @param userId
     * @return
     */
    Page<DrugUserCoupon> myCoupons(int page, int pageSize, Long userId);

    /**
     * 用户新增优惠券
     *
     * @param userCoupon
     * @return
     */
    int insertUserCoupon(DrugUserCoupon userCoupon);

    /**
     * 新增用户及用户地址
     *
     * @param address
     * @return

    int insertUserAndAddress(DrugUser user, DrugUserAddress address);
     */
    /**
     * 新增用户地址
     *
     * @param address
     * @return
     */
    int insertUserAddress(DrugUserAddress address);

    /**
     * 列表用户地址
     *
     * @param userId
     * @return
     */
    List<DrugUserAddress> myAddress(Long userId);

    /**
     * 删除用户地址
     *
     * @param id
     * @return
     */
    int changeUserAddressStatus(Long id, Long userId, Integer status);

    /**
     * 修改用户地址
     *
     * @param address
     * @return
     */
    int changeAddress(DrugUserAddress address);
    /**
     *
     * @param openid
     * @return
     */
    DrugUser getUserByOpenid(String openid);
}
