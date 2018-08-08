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
    private String errmsg;
    private Object data;

    public ResultObject(Integer code, String errmsg, Object data){
        this.code = code;
        this.errmsg = errmsg;
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

    public static ResultObject ok(Object o){
        return new ResultObject(ResultCode.OK.getCode(), null, o);
    }

    public static ResultObject fail(ResultCode r){
        return new ResultObject(r.getCode(), r.getMsg(), null);
    }

    public static ResultObject cond(boolean b, ResultCode r){
        if (b){
            return ok();
        } else {
            return fail(r);
        }
    }
}
