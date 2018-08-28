package com.wannengyongyao.drug.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class RewardVo {
    @Min(1)
    private Long orderId;
    @NotNull
    private BigDecimal amount;
}
