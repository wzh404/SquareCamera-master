package com.wannengyongyao.drug.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户代收药店
 */
@Data
public class DrugUserStore {
    private Long id;
    private Long userId;
    private Integer storeId;
    private LocalDateTime createTime;
}
