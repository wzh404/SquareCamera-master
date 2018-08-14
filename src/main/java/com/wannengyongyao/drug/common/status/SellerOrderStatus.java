package com.wannengyongyao.drug.common.status;

public enum SellerOrderStatus {
    INIT(0),
    // 用户取消卖家的报价，选择其他卖家
    CANCEL(-1),
    // 用户确认卖家的报价
    CONFIRM(1);

    private int code;

    public int get() {
        return code;
    }

    SellerOrderStatus(int code){
        this.code = code;
    }
}
