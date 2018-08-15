package com.wannengyongyao.drug.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * a=药费
 * b=服务费
 * c=悬赏费
 * d=运费
 * e=优惠券
 *
 * 卖家服务费提成=40%
 * 代收店提成=40%
 * 平台提成=30%
 *
 * 客户支出 = a + b + c + d - e
 * 卖家收入 = a + b*40% + c
 * 代收店收入 = b*40%
 * 平台收入 = b*30% + d
 * 平台支出 = e
 */
@Data
public class DrugSystemBalance {
    private Integer id;

    private Long sellerId;

    private Long orderId;

    private BigDecimal amount;

    private LocalDateTime createTime;

    private Integer debit;

    private String remark;

    private Integer classify;
}