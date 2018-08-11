package com.wannengyongyao.drug.util;

import com.google.common.base.Preconditions;

import javax.servlet.http.HttpServletRequest;

public class RequestUtil {
    public static long getUserId(HttpServletRequest request){
        Long userId = (Long) request.getAttribute(DrugConstants.REQUEST_ATTRIBUTE_UID);
        Preconditions.checkArgument(userId != null, DrugConstants.PLEASE_LOG_IN);

        return userId.longValue();
    }
}
