package com.wannengyongyao.drug.common.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@Component
public class SettlementRunner implements ApplicationRunner, Ordered, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(SettlementRunner.class);

    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(150));
    private static ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(10);

    @Override
    public void destroy() throws Exception {
        if (executor != null){
            executor.shutdown();
        }
    }

    /**
     * 加载未完成的结算任务
     *
     * @param args
     */
    @Override
    public void run(ApplicationArguments args) {

    }

    @Override
    public int getOrder() {
        return 2;
    }

    public static void execute(Runnable command){
        executor.execute(command);
    }

    /**
     * 24小时后执行
     *
     * @param command
     */
    public static  void schedule(Runnable command){
        scheduledThreadPool.schedule(command, 24, TimeUnit.HOURS);
    }
}
