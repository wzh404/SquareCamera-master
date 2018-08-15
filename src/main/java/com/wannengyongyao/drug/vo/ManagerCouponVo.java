package com.wannengyongyao.drug.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class ManagerCouponVo {
    @NotNull
    @Min(1)
    private Integer num;

    @NotNull
    @Min(1)
    private Integer amount;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
}
