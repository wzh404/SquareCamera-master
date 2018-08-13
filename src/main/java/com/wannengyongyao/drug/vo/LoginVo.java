package com.wannengyongyao.drug.vo;

import com.wannengyongyao.drug.model.DrugUser;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoginVo {
    private String mobile;
    // 手机验证码
    private String code;
    private String openid;

    public DrugUser asUser(){
        DrugUser user = new DrugUser();
        user.setMobile(this.mobile);
        user.setCreateTime(LocalDateTime.now());
        user.setLastUpdatedTime(LocalDateTime.now());
        user.setStatus(0);
        user.setOpenId(this.openid);

        return user;
    }
}
