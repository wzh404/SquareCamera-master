package com.wannengyongyao.drug.common.runner;

import com.wannengyongyao.drug.service.manager.ManagerSettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class ScheduleTask {
    // 结算调度
    private ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(10);

    @Autowired
    private ManagerSettlementService settlementService;

    /**
     * 24小时后执行结算
     *
     * @param orderId
     */
    public void submit(final Long orderId){
        submit(orderId, 24 * 60L);
    }

    /**
     *
     * @param orderId
     * @param minutes
     */
    public void submit(final Long orderId, final long minutes){
        scheduledThreadPool.schedule(
                () -> settlementService.settle(orderId),
                minutes, TimeUnit.MINUTES);
    }
}
