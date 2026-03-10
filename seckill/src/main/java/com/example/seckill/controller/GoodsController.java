package com.example.seckill.controller;

import com.example.seckill.entity.Goods;
import com.example.seckill.repository.GoodsRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GoodsController {

    private final GoodsRepository goodsRepository;

    public GoodsController(GoodsRepository goodsRepository) {
        this.goodsRepository = goodsRepository;
    }

    @GetMapping("/goods")
    public List<Goods> listGoods() {
        return goodsRepository.findAll();
    }
}