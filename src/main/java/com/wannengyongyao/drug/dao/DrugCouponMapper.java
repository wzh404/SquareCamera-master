package com.wannengyongyao.drug.dao;

import com.github.pagehelper.Page;
import com.wannengyongyao.drug.model.DrugCoupon;
import com.wannengyongyao.drug.model.DrugUserCoupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DrugCouponMapper {
    /**
     * 系统新生成优惠券
     *
     * @param coupon
     * @return
     */
    int insert(DrugCoupon coupon);

    /**
     * 我的优惠券
     *
     * @param userId
     * @return
     */
    Page<DrugUserCoupon> myCoupons(@Param("userId")Long userId);

    /**
     * 更新用户优惠券状态
     *
     * @param code
     * @return
     */
    int changeUserCouponStatus(@Param("code")String code, @Param("status")String status);

    /**
     * 根据优惠券码获取用户优惠券信息
     *
     * @param code
     * @return
     */
    DrugUserCoupon getUserCoupon(@Param("code")String code);

    /**
     * 用户新增优惠券
     *
     * @param userCoupon
     * @return
     */
    int insertUserCoupon(DrugUserCoupon userCoupon);

    /**
     *
     * @param code
     * @return
     */
    DrugCoupon get(@Param("code")String code);
}