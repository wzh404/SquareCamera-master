package com.wannengyongyao.drug.model;

import lombok.Data;

@Data
public class DrugUserWeixin {
    private Integer id;

    private String name;

    private Long userId;

    private String unionid;

    private String openid;

    private Integer gender;

    private String avatarUrl;

    private String country;

    private String province;

    private String city;
}