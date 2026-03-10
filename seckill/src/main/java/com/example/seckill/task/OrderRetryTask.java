package com.example.seckill.task;

import com.example.seckill.repository.OrderRepository;
import com.example.seckill.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class OrderRetryTask {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private OrderRepository orderRepository;

    /**
     * 每 60 秒扫描一次未完成的订单任务
     */
    @Scheduled(fixedDelay = 60000)
    public void retryUnfinishedTasks() {

        //查 status = 0 的任务（未完成）
        List<Map<String, Object>> tasks = taskRepository.findUnfinished();

        for (Map<String, Object> task : tasks) {
            Long userId = ((Number) task.get("user_id")).longValue();
            Long goodsId = ((Number) task.get("goods_id")).longValue();

            try {
                // 再次尝试创建订单
                orderRepository.createOrder(userId, goodsId);

                // 成功就标记 task
                taskRepository.markSuccess(userId, goodsId);

                System.out.println("补偿成功：user=" + userId + ", goods=" + goodsId);

            } catch (Exception e) {
                // 失败就留着，下次再试
                System.out.println("补偿失败，等待下次重试");
            }
        }
    }
}