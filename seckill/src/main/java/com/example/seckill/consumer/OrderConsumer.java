package com.example.seckill.consumer;

import com.example.seckill.entity.OrderMessage;
import com.example.seckill.repository.OrderRepository;
import com.example.seckill.repository.TaskRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.concurrent.TimeUnit;

@Component
public class OrderConsumer implements InitializingBean {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public void afterPropertiesSet() {

        new Thread(() -> {

            while (true) {

                try {

                    String msg = redisTemplate.opsForList()
                            .rightPop("seckill:order:queue");

                    if (msg == null) {
                        continue;
                    }

                    ObjectMapper mapper = new ObjectMapper();
                    String[] arr = msg.split(",");

                    Long userId = Long.valueOf(arr[0]);
                    Long goodsId = Long.valueOf(arr[1]);
                    Long taskId = Long.valueOf(arr[2]);

                    orderRepository.createOrder(userId, goodsId);

                    System.out.println("订单创建成功");


                    taskRepository.updateStatus(taskId, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }).start();
    }
}