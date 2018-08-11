package com.wannengyongyao.drug.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DrugInfo {
    private Integer drugId;

    private String drugName;

    private BigDecimal unitPrice;

    private String specifications;

    private Integer quantity;

    private String manufacturer;

    private String unit;
}
