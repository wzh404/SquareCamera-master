package com.wannengyongyao.drug.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DrugBanner {
    private Integer id;
    private Integer ord;
    private String imageUrl;
    private String url;
    private Integer classify;
    private LocalDateTime createTime;
}
