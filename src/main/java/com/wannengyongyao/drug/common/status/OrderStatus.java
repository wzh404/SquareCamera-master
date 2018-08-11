package com.wannengyongyao.drug.common.status;

public enum OrderStatus {
    INIT(0),
    CANCEL(-1),
    CONFIRM(1000),
    SHIPPED(1001),
    COMPLETED(1009);

    private int code;

    public int get() {
        return code;
    }

    OrderStatus(int code){
        this.code = code;
    }
}
