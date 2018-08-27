package com.wannengyongyao.drug.service.user.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wannengyongyao.drug.common.status.UserAddressStatus;
import com.wannengyongyao.drug.dao.*;
import com.wannengyongyao.drug.model.*;
import com.wannengyongyao.drug.service.user.DrugUserService;
import com.wannengyongyao.drug.vo.UserAddressVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service("drugUserService")
public class DrugUserServiceImpl implements DrugUserService {
    private final Logger logger = LoggerFactory.getLogger(DrugUserServiceImpl.class);

    @Autowired
    private DrugUserMapper userMapper;

    @Autowired
    private DrugUserWeixinMapper weixinMapper;

    @Autowired
    private DrugUserCartMapper userCartMapper;

    @Autowired
    private DrugMapper drugMapper;

    @Autowired
    private DrugCouponMapper couponMapper;

    @Autowired
    private DrugUserAddressMapper addressMapper;

    @Override
    public int insertUserPharmacist(DrugUserPharmacist pharmacist) {
        int cnt = userMapper.getUserPharmacist(pharmacist.getUserId(), pharmacist.getPharmacistId());
        if (cnt > 0){
            return -1;
        }
        return userMapper.insertUserPharmacist(pharmacist);
    }

    @Override
    public int insertUserStore(DrugUserStore store) {
        int cnt = userMapper.getUserStoreCount(store.getUserId(), store.getStoreId());
        if (cnt > 0){
            return -1;
        }
        return userMapper.insertUserStore(store);
    }

    @Override
    public List<DrugStore> getUserStores(Long userId) {
        return userMapper.getUserStores(userId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int insertWeixinUser(DrugWeixinUser wxUser) {
        DrugWeixinUser u = weixinMapper.getByOpenId(wxUser.getOpenId());
        if (u != null){
            return 1;
        }
        return weixinMapper.insert(wxUser);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Long insertWeixinAndUser(DrugWeixinUser wxUser) {
        DrugWeixinUser u = weixinMapper.getByOpenId(wxUser.getOpenId());
        if (u != null){
            logger.warn("openid already exists in wx user.");
            DrugUser du = userMapper.getUserByOpenid(wxUser.getOpenId());
            if (du == null){
                logger.warn("openid not exists in user.");
                return -1L;
            }
            return du.getId();
        }

        DrugUser user = new DrugUser();
        user.setName(wxUser.getNickName());
        user.setGender("1".equals(wxUser.getGender()) ? 1 : 0);
        user.setCreateIp("localhost");
        user.setOpenId(wxUser.getOpenId());
        user.setMobile("-");
        user.setCreateTime(LocalDateTime.now());
        user.setLastUpdatedTime(LocalDateTime.now());
        user.setStatus(0);
        user.setAvatar(wxUser.getAvatarUrl());
        user.setCreateIp("localhost");

        userMapper.insert(user);
        weixinMapper.insert(wxUser);
        return user.getId();
    }

    @Override
    public int insertUser(DrugUser user) {
        DrugUser u = userMapper.getByMobile(user.getMobile());
        if (u != null){
            user.setId(u.getId());
            return 1;
        }

        DrugWeixinUser w = weixinMapper.getByOpenId(user.getOpenId());
        if (w == null){
            return -2;
        }
        user.setName(w.getNickName());
        user.setGender("1".equalsIgnoreCase(w.getGender()) ? 0 : 1);

        return userMapper.insert(user);
    }

    @Override
    public DrugWeixinUser getByOpenId(String openId) {
        return weixinMapper.getByOpenId(openId);
    }

    @Override
    public int insert(DrugUserCart cart) {
        Drug drug = drugMapper.get(cart.getDrugId());
        if (drug == null){
            return -1;
        }

        DrugUserCart c = userCartMapper.getUserCart(cart.getUserId(), cart.getDrugId());
        if (c != null){
            userCartMapper.changeUserCartQuantity(cart.getUserId(), cart.getDrugId(), 1);
            return 1;
        }
        cart.setDrugName(drug.getName());
        cart.setManufacturer(drug.getManufacturer());
        cart.setSpecifications(drug.getSpecifications());
        cart.setQuantity(1);
        cart.setUnit("件");
        cart.setUnitPrice(BigDecimal.valueOf(0.0));
        return userCartMapper.insert(cart);
    }

    @Override
    public List<DrugUserCart> listUserCart(Long userId) {
        return userCartMapper.listUserCart(userId);
    }

    @Override
    public int deleteUserCart(List<DrugUserCart> carts) {
        return userCartMapper.deleteUserCart(carts);
    }

    @Override
    public List<DrugUserLongterm> listUserLongTerm(Long userId) {
        return userMapper.listUserLongTerm(userId);
    }

    @Override
    public int insertUserLongTerm(DrugUserLongterm longterm) {
        Drug drug = drugMapper.get(longterm.getDrugId());
        if (drug == null){
            return -1;
        }

        // 长期用药药品已存在
        DrugUserLongterm c = userMapper.getUserLongTerm(longterm.getUserId(), longterm.getDrugId());
        if (c != null){
            return -2;
        }

        // 历史药品下单数超过2
        int cnt = userMapper.getUserOrderDrugCount(longterm.getUserId(), longterm.getDrugId());
        if (cnt < 2) {
            return -3;
        }

        longterm.setDrugName(drug.getName());
        longterm.setManufacturer(drug.getManufacturer());
        longterm.setSpecifications(drug.getSpecifications());
        longterm.setQuantity(1);
        longterm.setUnit("件");
        longterm.setUnitPrice(BigDecimal.valueOf(0.0));

        return userMapper.insertUserLongTerm(longterm);
    }

    @Override
    public int deleteUserLongTerm(List<DrugUserLongterm> longterms) {
        return userMapper.deleteUserLongTerm(longterms);
    }

    @Override
    public Page<DrugUserCoupon> myCoupons(int page, int pageSize, Long userId) {
        PageHelper.startPage(page, pageSize);
        return couponMapper.myCoupons(userId);
    }

    @Override
    public int insertUserCoupon(DrugUserCoupon userCoupon) {
        DrugCoupon c = couponMapper.get(userCoupon.getCode());
        if (c == null){
            return -1;
        }
        DrugUserCoupon uc = couponMapper.getUserCoupon(userCoupon.getCode());
        if (uc != null){
            return -2;
        }
        return couponMapper.insertUserCoupon(userCoupon);
    }

    /*
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int insertUserAndAddress(DrugUser user, DrugUserAddress address) {
        userMapper.insert(user);
        address.setUserId(user.getId());
        return addressMapper.insert(address);
    }
*/
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int insertUserAddress(DrugUserAddress address) {
        if (address.getStatus() == UserAddressStatus.DEFAULT.get()){
            addressMapper.undefaultStatus(address.getUserId());
        }
        return addressMapper.insert(address);
    }

    @Override
    public List<DrugUserAddress> myAddress(Long userId) {
        return addressMapper.list(userId);
    }

    @Override
    public DrugUserAddress addressDetail(Long id) {
        return addressMapper.get(id);
    }

    @Override
    public int changeUserAddressStatus(Long id, Long userId, Integer status) {
        DrugUserAddress a = addressMapper.get(id);
        if (a == null){
            return -1;
        }
        if (a.getUserId().longValue() != userId.longValue()){
            return -2;
        }
        if (status == UserAddressStatus.DEFAULT.get()){
            addressMapper.undefaultStatus(userId);
        }
        return addressMapper.changeStatus(id, status);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int changeAddress(DrugUserAddress address) {
        if (address.getStatus() == UserAddressStatus.DEFAULT.get()){
            addressMapper.undefaultStatus(address.getUserId());
        }

        return addressMapper.changeAddress(address);
    }

    @Override
    public DrugUser getUserByOpenid(String openid) {
        return userMapper.getUserByOpenid(openid);
    }
}
