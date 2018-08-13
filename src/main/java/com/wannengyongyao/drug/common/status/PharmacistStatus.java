package com.wannengyongyao.drug.common.status;

public enum PharmacistStatus {
    REGISTER(0),
    DELETED(-1),
    NORMAL(1);

    private int code;

    public int get() {
        return code;
    }

    PharmacistStatus(int code){
        this.code = code;
    }
}
