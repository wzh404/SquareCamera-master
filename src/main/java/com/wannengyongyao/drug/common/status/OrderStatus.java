package com.wannengyongyao.drug.common.status;

public enum OrderStatus {
    INIT(0),
    CANCEL(-1),
    CONFIRM(1000),
    PAYMENT(1001),
    SHIPPED(1008),
    COMPLETED(1009);

    private int code;

    public int get() {
        return code;
    }

    OrderStatus(int code){
        this.code = code;
    }
}
