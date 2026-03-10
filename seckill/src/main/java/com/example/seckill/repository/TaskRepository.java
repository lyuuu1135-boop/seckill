package com.example.seckill.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

@Repository
public class TaskRepository {

    @Autowired
    private  JdbcTemplate jdbcTemplate;

    public void updateStatus(Long taskId, int status) {

        String sql = "update seckill_order_task set status=? where id=?";

        jdbcTemplate.update(sql, status, taskId);

    }

    public Long createTask(Long userId, Long goodsId) {

        String sql = "insert into seckill_order_task(user_id, goods_id, status, create_time) values (?, ?, 0, now())";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, userId);
            ps.setLong(2, goodsId);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void markSuccess(Long userId, Long goodsId) {
        String sql = """
            update seckill_order_task
            set status = 1
            where user_id = ? and goods_id = ?
        """;
        jdbcTemplate.update(sql, userId, goodsId);
    }

    public void markFail(Long userId, Long goodsId) {
        String sql = """
            update seckill_order_task
            set status = 2
            where user_id = ? and goods_id = ?
        """;
        jdbcTemplate.update(sql, userId, goodsId);
    }
    public List<Map<String, Object>> findUnfinished() {
        String sql = """
        select user_id, goods_id
        from seckill_order_task
        where status = 0
    """;
        return jdbcTemplate.queryForList(sql);
    }

}
