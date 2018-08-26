package com.wannengyongyao.drug.common.runner;

import com.wannengyongyao.drug.model.DrugOrder;
import com.wannengyongyao.drug.service.manager.ManagerSettlementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class SettlementRunner implements ApplicationRunner, Ordered, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(SettlementRunner.class);

    @Autowired
    private ManagerSettlementService settlementService;

    @Autowired
    private ScheduleTask task;

    @Override
    public void destroy() throws Exception {
        logger.info("settle runner destroy.");
    }

    /**
     * 加载未完成的结算任务
     *
     * @param args
     */
    @Override
    public void run(ApplicationArguments args) {
        List<DrugOrder> orders = settlementService.listUnSettlementOrder();
        if (orders == null || orders.isEmpty()){
            return;
        }

        orders.stream().forEach(order -> {
            LocalDateTime t = order.getSignTime();
            long minutes = Duration.between(t, LocalDateTime.now()).get(ChronoUnit.SECONDS) / 60;
            if (minutes >= 24 * 60) {
                minutes = 0;
            } else {
                minutes = 24 * 60 - minutes;
            }
            logger.info("submit order {} after {} minute", order.getId(), minutes);
            task.submit(order.getId(), minutes);
        });
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
