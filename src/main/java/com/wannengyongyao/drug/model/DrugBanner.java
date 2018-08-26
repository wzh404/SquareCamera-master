package com.wannengyongyao.drug.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DrugBanner {
    private Integer id;
    private Integer ord;
    private String imageUrl;
    private String url;
    private Integer classify;
    private LocalDateTime createTime;
}
