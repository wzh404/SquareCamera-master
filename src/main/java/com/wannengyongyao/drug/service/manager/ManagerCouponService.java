package com.wannengyongyao.drug.service.manager;

import java.time.LocalDate;

public interface ManagerCouponService {
    /**
     * 批量生成优惠券
     *
     * @param num  生成数量
     * @param amount 面值
     * @param startDate 有效期开始日期
     * @param endDate 有效期结束日期
     * @return
     */
    int batchInsert(int num, int amount, LocalDate startDate, LocalDate endDate);
}
