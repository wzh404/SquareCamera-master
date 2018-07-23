package com.wannengyongyao.drug.util;

import com.wannengyongyao.drug.common.ResultObject;
import com.github.pagehelper.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class StringUtil {
    private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);

    public static Map<String, Object> pageToMap(String key, Page page){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(key, page.getResult());
        resultMap.put("page", ResultObject.pageMap(page));
        return resultMap;
    }
}
