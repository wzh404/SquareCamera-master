package com.wannengyongyao.drug.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DrugSystemAccount {
    private Integer id;
    private BigDecimal expenditure;
    private BigDecimal income;
    private LocalDateTime lastUpdatedTime;
}
