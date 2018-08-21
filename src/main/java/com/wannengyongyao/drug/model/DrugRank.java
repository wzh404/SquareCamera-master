package com.wannengyongyao.drug.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 药品排名
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DrugRank {
    private Integer id;
    private Integer drugId;
    private Integer rank;
    private LocalDateTime lastUpdatedTime;

    private String drugName;
    private String specifications;
    private String manufacturer;
}
