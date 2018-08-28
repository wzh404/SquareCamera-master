package com.wannengyongyao.drug.vo;

import lombok.Data;

@Data
public class ShippingVo {
    private Long orderId;
    private String shippingCompany;
    private String shippingId;
}
