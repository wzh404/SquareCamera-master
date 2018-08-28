package com.wannengyongyao.drug.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ShippingVo {
    @NotNull
    private Long orderId;
    @NotEmpty(message = "快递公司不能为空")
    private String shippingCompany;
    @NotEmpty(message = "快递单号不能为空")
    private String shippingId;
}
