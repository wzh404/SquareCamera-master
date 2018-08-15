package com.wannengyongyao.drug.service.manager.impl;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.wannengyongyao.drug.common.status.CouponStatus;
import com.wannengyongyao.drug.dao.DrugCouponMapper;
import com.wannengyongyao.drug.model.DrugCoupon;
import com.wannengyongyao.drug.service.manager.ManagerCouponService;
import com.wannengyongyao.drug.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service("managerCouponService")
public class ManagerCouponServiceImpl implements ManagerCouponService {
    private final Logger logger = LoggerFactory.getLogger(ManagerCouponServiceImpl.class);

    @Autowired
    private DrugCouponMapper couponMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int batchInsert(int num, int amount, LocalDate startDate, LocalDate endDate) {
        DrugCoupon coupon = new DrugCoupon();
        coupon.setAmount(amount);
        coupon.setStartDate(startDate);
        coupon.setEndDate(endDate);
        coupon.setCreateTime(LocalDateTime.now());
        coupon.setStatus(CouponStatus.NORMAL.get());

        int i = 0;
        while (i < num){
            String code = StringUtil.getCouponCode(6);
            coupon.setCode(code);
            try {
                couponMapper.insert(coupon);
            } catch (DataAccessException e){
                final Throwable cause = e.getCause();
                if(cause instanceof MySQLIntegrityConstraintViolationException){
                    logger.warn("Constraint Violation", e);
                    continue;
                }

                throw e;
            }

            i++;
        }
        return i;
    }
}
