package com.code.monks.nukkad.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Configuration
public class FirebaseImageProcessThreadPoolConfig {


    private static final AtomicInteger threadCounter = new AtomicInteger(1);
    private static final int CORE_POOL_SIZE = 20;
    private static final int MAX_POOL_SIZE = 20;
    private static final long KEEP_ALIVE_SECONDS = 60L;
    private static final int QUEUE_CAPACITY = 1000;
    public static final String IMAGE_UPLOAD_EXECUTOR = "imageUploadExecutor";

    @Bean(name = FirebaseImageProcessThreadPoolConfig.IMAGE_UPLOAD_EXECUTOR)
    public ExecutorService imageUploadExecutor() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_SECONDS,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(QUEUE_CAPACITY),
                runnable -> {
                    Thread thread = new Thread(runnable);
                    thread.setName("image-upload-thread-" + threadCounter.getAndIncrement());
                    thread.setDaemon(true);
                    return thread;
                },
                // if queue full — caller runs task instead of rejecting
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        log.info("[THREAD POOL] imageUploadExecutor created — "
                        + "coreSize: {}, maxSize: {}, queueCapacity: {}",
                CORE_POOL_SIZE, MAX_POOL_SIZE, QUEUE_CAPACITY);

        return executor;
    }
}
