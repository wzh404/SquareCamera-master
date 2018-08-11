package com.wannengyongyao.drug.util;

import com.wannengyongyao.drug.common.ResultObject;
import com.github.pagehelper.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static java.time.LocalDate.now;
import static java.time.LocalDate.of;

public class StringUtil {
    private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);
    private static long lastDays = 0L;
    private static long lastSecs = 0L;

    /**
     * 生成订单
     *
     * @param uid
     * @return
     */
    public static long getOrderId(long uid) {
        uid = uid % 100;
        long sequence = 1000L;

        long days = ChronoUnit.DAYS.between(of(2015, 10, 1), now());
        int secs = LocalTime.now().toSecondOfDay();

        if (lastDays == days && lastSecs == secs) {
            sequence++;
        } else {
            sequence = 1001;
        }
        String orderId = String.format("%d%d%04d%d", days, secs, sequence % 10000, uid);
        lastDays = days;
        lastSecs = secs;

        return Long.valueOf(orderId).longValue();
    }

    /**
     * 生成短信验证码
     *
     * @param length
     * @return
     */
    public static String getSmsCode(int length) {
        String str = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            sb.append(str.charAt(random.nextInt(10)));
        }
        return sb.toString();
    }
}
