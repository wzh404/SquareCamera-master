package com.wannengyongyao.drug.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class DrugOrderGoods  {
    private Integer id;

    private String orderId;

    private Integer drugId;

    private String drugName;

    private BigDecimal unitPrice;

    private String specifications;

    private Integer quantity;

    private String manufacturer;
}