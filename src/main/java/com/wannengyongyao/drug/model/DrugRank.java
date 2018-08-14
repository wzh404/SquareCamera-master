package com.wannengyongyao.drug.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 药品排名
 */
@Data
public class DrugRank {
    private Integer id;
    private Integer drugId;
    private Integer rank;
    private LocalDateTime lastUpdatedTime;

    private String drugName;
    private String specifications;
    private String manufacturer;
}
