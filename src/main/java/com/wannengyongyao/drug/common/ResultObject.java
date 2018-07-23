package com.wannengyongyao.drug.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.pagehelper.Page;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by @author wangzunhui on 2018/3/15.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultObject {
    private Integer code;
    private String message;
    private Object data;

    public ResultObject(Integer code, String error, Object data){
        this.code = code;
        this.message = error;
        this.data = data;
    }

    public static ResultObject ok(String key, Object val){
        Map<String, Object> map = new HashMap();
        map.put(key, val);
        return new ResultObject(ResultCode.OK.getCode(), null, map);
    }

    public static ResultObject pageMap(Page page, String key){
        HashMap<String, Object> pageMap = new HashMap<>();
        pageMap.put("total", page.getTotal());
        pageMap.put("pages", page.getPages());
        pageMap.put("pageNum", page.getPageNum());
        pageMap.put(key, page.getResult());

        return new ResultObject(ResultCode.OK.getCode(), null, pageMap);
    }

    public static ResultObject ok(){
        return new ResultObject(ResultCode.OK.getCode(), null, null);
    }

    public static ResultObject ok(Object r){
        return new ResultObject(ResultCode.OK.getCode(), null, r);
    }

    public static ResultObject fail(ResultCode code){
        return new ResultObject(code.getCode(), null, null);
    }

    public static ResultObject fail(ResultCode code, String error){
        return new ResultObject(code.getCode(), error, null);
    }

    public static ResultObject cond(boolean b, ResultCode r){
        if (b){
            return ok();
        } else {
            return fail(r);
        }
    }
}
