package com.ljh.main.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 该配置类用于定制Gson对象
 * 主要用于项目中JSON数据的序列化和反序列化
 */
@Configuration
public class GsonConfig {

    /**
     * 创建并返回一个定制的Gson对象
     * 该对象设置了日期格式，以适应特定的日期时间格式需求
     *
     * @return 定制化的Gson对象
     */
    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .create();
    }
}

