package com.wannengyongyao.drug.model;

import lombok.Data;

@Data
public class DrugWeixinUser {
    private Long id;

    private String nickName;

    private Long userId;

    private String unionId;

    private String openId;

    private String gender;

    private String avatarUrl;

    private String country;

    private String province;

    private String city;
}