package com.wannengyongyao.drug.model;

import lombok.Data;


import java.math.BigDecimal;
import java.util.Date;

@Data
public class DrugUserCart {
    private Integer id;

    private Long userId;

    private String goodsName;

    private BigDecimal goodsPrice;

    private String goodsSpecifications;

    private Integer goodsNumber;

    private String goodsVendor;

    private Date createTime;


}