package com.wannengyongyao.drug.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wannengyongyao.drug.common.status.CouponStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DrugUserCoupon {
    private Integer id;
    private Long userId;
    private String code;
    private Integer amount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private Integer status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;

    public Integer getStatus(){
        if (status == CouponStatus.NORMAL.get()){
            return this.endDate.isBefore(LocalDate.now()) ? CouponStatus.EXPIRED.get() : this.status;
        }

        return this.status;
    }
}
