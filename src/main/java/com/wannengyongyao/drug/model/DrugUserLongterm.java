package com.wannengyongyao.drug.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户长期用药
 */
@Data
public class DrugUserLongterm extends DrugInfo{
    private Integer id;
    private Long userId;
    private LocalDateTime createTime;
}
