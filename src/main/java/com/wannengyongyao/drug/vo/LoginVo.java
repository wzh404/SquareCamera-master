package com.wannengyongyao.drug.vo;

import com.wannengyongyao.drug.model.DrugUser;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
public class LoginVo {
    @NotEmpty(message = "手机号不能为空")
    @Length(min=11, max=11, message = "非法手机号")
    private String mobile;
    // 手机验证码
    @NotEmpty(message = "手机验证码不能为空")
    @Length(min=6, max=6, message = "手机验证码必须为6位")
    private String code;

    @NotEmpty(message = "微信openid不能为空")
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
