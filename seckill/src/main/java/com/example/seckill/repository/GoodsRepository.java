package com.example.seckill.repository;

import com.example.seckill.entity.Goods;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GoodsRepository {

    private final JdbcTemplate jdbcTemplate;

    public GoodsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Goods> findAll() {
        String sql = "SELECT id, name FROM goods";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Goods goods = new Goods();
            goods.setId(rs.getLong("id"));
            goods.setName(rs.getString("name"));
            return goods;
        });
    }
    // 扣减库存（乐观锁）
    public int decreaseStock(Long id, int goodsId, int version) {
        String sql = """
            UPDATE goods
            SET stock = stock - 1,
                version = version + 1
            WHERE id = ? AND version = ? AND stock > 0
        """;
        return jdbcTemplate.update(sql, goodsId, version);
    }

    // 查询当前版本号
    public Integer getVersion(Long id, int goodsId) {
        String sql = "SELECT version FROM goods WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, goodsId);
    }

    public Integer getStockById(Long goodsId){

        String sql = "select stock from goods where id = ?";

        return jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                goodsId
        );
    }
}
