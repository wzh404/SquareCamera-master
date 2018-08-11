package com.wannengyongyao.drug.service.user.impl;

import com.wannengyongyao.drug.dao.DrugUserMapper;
import com.wannengyongyao.drug.dao.DrugUserWeixinMapper;
import com.wannengyongyao.drug.model.*;
import com.wannengyongyao.drug.service.user.DrugUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("drugUserService")
public class DrugUserServiceImpl implements DrugUserService {
    @Autowired
    private DrugUserMapper userMapper;

    @Autowired
    private DrugUserWeixinMapper weixinMapper;

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
    public int insertWeixinUser(DrugWeixinUser user) {
        DrugWeixinUser u = weixinMapper.getByOpenId(user.getOpenId());
        if (u != null){
            return -1;
        }
        return weixinMapper.insert(user);
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
        user.setGender("男".equalsIgnoreCase(w.getGender()) ? 0 : 1);

        return userMapper.insert(user);
    }
}
