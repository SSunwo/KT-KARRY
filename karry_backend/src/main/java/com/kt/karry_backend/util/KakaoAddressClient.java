package com.kt.karry_backend.util;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


import org.springframework.http.ResponseEntity;
import java.util.Map;

@FeignClient(name = "kakaoAddressClient", url = "https://dapi.kakao.com")
public interface KakaoAddressClient {

    @GetMapping("/v2/local/search/address.json")
    ResponseEntity<Map> getCoordinates(
        @RequestParam("query") String address,
        @RequestHeader("Authorization") String apiKey
    );
}