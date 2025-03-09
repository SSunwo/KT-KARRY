package com.kt.karry_backend.util;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kt.karry_backend.config.NaverMapClientConfig;

import org.springframework.http.ResponseEntity;
import java.util.Map;

@FeignClient(name = "naverMapClient", url = "https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving", configuration = NaverMapClientConfig.class)
public interface NaverMapClient {

    @GetMapping
    ResponseEntity<Map> getRoute(
            @RequestParam("start") String start,
            @RequestParam("goal") String goal
    );
}