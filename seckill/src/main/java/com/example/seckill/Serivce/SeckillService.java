package com.example.seckill.Serivce;

import com.example.seckill.entity.OrderMessage;
import com.example.seckill.repository.GoodsRepository;
import com.example.seckill.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class SeckillService {

    @Autowired
    private TaskRepository taskRepository;
    private final StringRedisTemplate redisTemplate;
    private final GoodsRepository goodsRepository;

    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;

    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setResultType(Long.class);
        SECKILL_SCRIPT.setScriptText("""
            --- 1. 限流
                     local current = redis.call('INCR', KEYS[3])
                     if current == 1 then
                         redis.call('EXPIRE', KEYS[3], 1)
                     end
                     if current > tonumber(ARGV[1]) then
                         return 1 -- 限流
                     end
                
                     -- 2. 防重复（用户 + 商品） 
                     if redis.call('EXISTS', KEYS[2]) == 1 then
                         return 2 -- 已秒杀
                     end
                
                     -- 3. 校验库存
                     local stock = tonumber(redis.call('GET', KEYS[1]))
                     if stock <= 0 then
                         return 3 -- 库存不足
                     end
                
                     -- 4. 扣库存
                     redis.call('DECR', KEYS[1])
                
                     -- 5. 标记用户（最后一步）
                     redis.call('SET', KEYS[2], '1')
                     redis.call('EXPIRE', KEYS[2], tonumber(ARGV[2]))
                
                     return 0 -- 成功
        """);
    }

    public SeckillService(StringRedisTemplate redisTemplate,
                          GoodsRepository goodsRepository) {
        this.redisTemplate = redisTemplate;
        this.goodsRepository = goodsRepository;
    }

    public String doSeckill(Long goodsId, Long userId) {

        String stockKey = "seckill:stock:" + goodsId;
        String userKey  = "seckill:user:" + goodsId + ":" + userId;

        long second = System.currentTimeMillis() / 1000;
        String limitKey = "seckill:limit:" + second;

        List<String> keys = Arrays.asList(stockKey, userKey, limitKey);

        Long result = redisTemplate.execute(
                SECKILL_SCRIPT,
                keys,
                "5",        // 每秒最多 5 个请求
                "86400"     // 用户标记 1 天
        );

        if (result == null) {
            return "系统异常";
        }

        // 秒杀成功
        if (result == 0) {


            Long taskId = taskRepository.createTask(userId, goodsId);

            try {

                String msg = userId + "," + goodsId + "," + taskId;

                redisTemplate.opsForList().leftPush(
                        "seckill:order:queue",
                        msg
                );

            } catch (Exception e) {
                e.printStackTrace();
            }

            return "秒杀成功，正在生成订单";
        }

        if (result == 2) {
            return "您已参加过秒杀";
        }

        if (result == 3) {
            return "库存不足";
        }

        if (result == 1) {
            return "请求过于频繁";
        }

        return "未知错误";
    }
}