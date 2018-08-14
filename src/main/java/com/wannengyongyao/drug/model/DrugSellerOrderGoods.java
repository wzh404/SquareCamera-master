package com.wannengyongyao.drug.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DrugSellerOrderGoods  {
    private Integer id;

    private Long orderId;

    private Integer orderDrugId;

    private Integer drugId;

    private String drugName;

    private BigDecimal unitPrice;

    private String specifications;

    private Integer quantity;

    private String manufacturer;

    private String photos;

    private String remark;

    private Long sellerId;
}