package com.wannengyongyao.drug.vo;

import com.wannengyongyao.drug.common.status.UserAddressStatus;
import com.wannengyongyao.drug.model.DrugUser;
import com.wannengyongyao.drug.model.DrugUserAddress;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
public class UserRegisterVo {
    private String openid;
    @NotEmpty(message = "手机号不能为空")
    private String mobile;
    private String address;
    private String code;
    private String district;
    private String name;

    private Long userId;

    public DrugUserAddress asAddress(){
        DrugUserAddress address = new DrugUserAddress();
        address.setAddress(this.address);
        address.setDistrict(this.district);
        address.setCreateTime(LocalDateTime.now());
        address.setStatus(UserAddressStatus.DEFAULT.get());
        address.setUserId(this.userId);
        address.setName(this.name);

        return address;
    }

    public DrugUser asUser(){
        DrugUser user = new DrugUser();
        user.setMobile(this.mobile);
        user.setCreateTime(LocalDateTime.now());
        user.setLastUpdatedTime(LocalDateTime.now());
        user.setStatus(0);
        user.setCreateIp("localhost");
        user.setOpenid(this.openid);

        return user;
    }
}

