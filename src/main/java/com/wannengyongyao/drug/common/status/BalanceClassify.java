package com.wannengyongyao.drug.common.status;

public enum BalanceClassify {
    SHIPPED(1000), // 运费
    DRUG(1001),    // 药费
    SERVICE(1002), // 服务费
    REWARD(1003),  // 悬赏费
    COUPON(1004);  // 优惠费

    private int code;

    public int get() {
        return code;
    }

    BalanceClassify(int code){
        this.code = code;
    }
}
