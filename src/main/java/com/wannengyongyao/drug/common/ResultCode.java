package com.wannengyongyao.drug.common;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * Created by @author wangzunhui on 2018/3/15.
 */
public enum ResultCode {
    OK(0),
    USER_PLEASE_LOGIN(1000),
    UPLOAD_FILE_FAILED(1001);

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
            .build();
}