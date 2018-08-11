package com.wannengyongyao.drug.common;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * Created by @author wangzunhui on 2018/3/15.
 */
public enum ResultCode {
    OK(0),
    USER_PLEASE_LOGIN(1000),
    UPLOAD_FILE_FAILED(1001),
    USER_PHARMACIST_EXIST(2001),
    ORDER_NOT_EXIST(3001),
    ORDER_CANCELED(3002),
    ORDER_CANNOT_CANCEL(3003),
    INVALID_SMS_CODE(4001),
    FAILED(9999);

    private int code;

    public int getCode() {
        return code;
    }

    public String getMsg(){
        return errmsgMap.get(code);
    }

    ResultCode(int code){
        this.code = code;
    }

    private static Map<Integer, String> errmsgMap = ImmutableMap.<Integer, String>builder()
            .put(USER_PLEASE_LOGIN.code, "请登录")
            .put(UPLOAD_FILE_FAILED.code, "文件上传失败")
            .put(USER_PHARMACIST_EXIST.code, "药师已结交")
            .put(ORDER_NOT_EXIST.code, "订单不存在")
            .put(ORDER_CANCELED.code, "订单已取消")
            .put(ORDER_CANNOT_CANCEL.code, "订单不能取消")
            .put(INVALID_SMS_CODE.code, "无效的手机验证码")
            .build();
}