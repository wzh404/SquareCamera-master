package com.wannengyongyao.drug.common.status;

public enum OrderStatus {
    INIT(0),
    CONFIRM(1000);

    private int code;

    public int get() {
        return code;
    }

    OrderStatus(int code){
        this.code = code;
    }
}
