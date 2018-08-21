package com.wannengyongyao.drug.common.status;

public enum UserAddressStatus {
    OK(0),
    DELETED(-1),
    DEFAULT(1);

    private int code;

    public int get() {
        return code;
    }

    UserAddressStatus(int code){
        this.code = code;
    }
}
