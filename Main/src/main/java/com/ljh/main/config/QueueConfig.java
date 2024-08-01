package com.ljh.main.config;

import com.ljh.main.ScopeTask.Dto.TaskDto;
import com.ljh.main.ScopeTask.ProducerConsumerModel.Consumer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// QueueBean类
@Configuration
public class QueueConfig {

    @Bean
    public BlockingQueue<TaskDto> taskQueue() {
        return new LinkedBlockingQueue<>();
    }

    @Bean
    public CommandLineRunner startConsumer(Consumer consumer) {
        return args -> {
            Thread consumerThread = new Thread(consumer);
            consumerThread.setDaemon(true); // 设置为守护线程，以便在主程序结束时自动终止
            consumerThread.start();
        };
    }
}