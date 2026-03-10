package com.example.seckill.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createOrder(Long userId, Long goodsId) {
        try {
            jdbcTemplate.update(
                    "insert into seckill_order(user_id, goods_id) values (?, ?)",
                    userId, goodsId
            );
        } catch (DuplicateKeyException e) {
            // 订单已经存在，忽略
        }
    }

}
