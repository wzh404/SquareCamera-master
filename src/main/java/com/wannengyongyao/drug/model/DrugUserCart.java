package com.wannengyongyao.drug.model;

import lombok.Data;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

// 用户购物车
@Data
public class DrugUserCart extends DrugInfo{
    private Integer id;

    private Long userId;

    private LocalDateTime createTime;
}