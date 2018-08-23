package com.wannengyongyao.drug.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DrugUserAddress {
    private Long id;
    private Long userId;
    private String name;
    private String district;
    private String city;
    private String province;
    private String address;
    private String mobile;
    private LocalDateTime createTime;
    private Integer status;
    private String cityName;

    public String getCity(){
        return this.district.substring(0,4) + "00";
    }

    public String getProvince(){
        return this.district.substring(0,2) + "0000";
    }
}
