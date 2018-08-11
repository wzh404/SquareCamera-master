package com.wannengyongyao.drug.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户结交药师
 */
@Data
public class DrugUserPharmacist {
    private Long userId;
    private Integer pharmacistId;
    private LocalDateTime createTime;
}
