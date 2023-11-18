package com.geovis.receiver.thread;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/***
 * 类描述: 线程池
 * @author yxl
 * @Date: 10:01 2020/9/19
 */
@Data
@Slf4j
@Component
@EnableAsync
@PropertySource(value = "classpath:application-thread.yml")
public class ThreadPoolConfig {

    @Value("${threadpool.message.corePoolSize}")
    private int corePoolSize;

    @Value("${threadpool.message.maxPoolSize}")
    private int maxPoolSize;

    @Value("${threadpool.message.queueCapacity}")
    private int queueCapacity;

    @Value("${threadpool.message.keepAliveSeconds}")
    private String keepAliveSeconds;

    @Value("${threadpool.message.threadName}")
    private String threadName;

    @Bean(name = "asyncServiceExecutor")
    public Executor asyncServiceExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(corePoolSize);
        //配置最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        //配置队列大小
        executor.setQueueCapacity(queueCapacity);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix(threadName);

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }
}
