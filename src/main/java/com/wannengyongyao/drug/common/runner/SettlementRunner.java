package com.wannengyongyao.drug.common.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class SettlementRunner implements ApplicationRunner, Ordered, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(SettlementRunner.class);

    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(150));

    @Override
    public void destroy() throws Exception {
        if (executor != null){
            executor.shutdown();
        }
    }

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
}
