package com.kt.karry_backend.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NaverMapClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("x-ncp-apigw-api-key-id", "kgae9y1z0e");
            requestTemplate.header("x-ncp-apigw-api-key", "7PQSWUhfQA47NAv5rSVWniwYmtVauquBQT7EqQJv");
        };
    }
}