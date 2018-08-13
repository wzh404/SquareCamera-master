package com.wannengyongyao.drug.common.status;

public enum ShippingStatus {
    INIT(0),
    SHIPPED(1), // 卖家已发货
    COLLECTION(2), // 代收店已收货
    COMPLETED(9); // 用户已取货

    private int code;

    public int get() {
        return code;
    }

    ShippingStatus(int code){
        this.code = code;
    }
}
