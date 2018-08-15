package com.wannengyongyao.drug.common.status;

public enum CouponStatus {
    NORMAL(1),
    USED(0),
    CANCEL(-1),
    EXPIRED(-2);

    private int code;

    public int get() {
        return code;
    }

    CouponStatus(int code){
        this.code = code;
    }
}
