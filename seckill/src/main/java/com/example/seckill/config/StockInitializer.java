package com.example.seckill.config;

import com.example.seckill.repository.GoodsRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

public class StockInitializer {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostConstruct
    public void initStock() {

        Integer stock = goodsRepository.getStockById(1L);

        redisTemplate.opsForValue().set(
                "seckill:stock:1",
                String.valueOf(stock)
        );

        System.out.println("库存预热完成，stock=" + stock);
    }
}
