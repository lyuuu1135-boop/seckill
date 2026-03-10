package com.example.seckill.controller;

import com.example.seckill.Serivce.SeckillService;
import com.example.seckill.repository.GoodsRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SeckillController {

    private final SeckillService seckillService;

    public SeckillController(SeckillService seckillService) {
        this.seckillService = seckillService;
    }

    @GetMapping("/seckill")
    public String seckill(@RequestParam Long userId){

        Long goodsId = 1L;

        return seckillService.doSeckill(goodsId, userId);
    }
}