package com.example.seckill.controller;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockController {

    private final StringRedisTemplate redisTemplate;

    public StockController(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/init-stock")
    public String initStock() {
        // 假设商品 id = 1，库存 = 10
        redisTemplate.opsForValue().set("seckill:stock:1", "10");
        return "stock init success";
    }
}