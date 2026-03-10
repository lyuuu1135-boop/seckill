package com.example.seckill.entity;

public class OrderMessage {

    private Long userId;
    private Long goodsId;
    private Long taskId;

    public OrderMessage() {
    }

    public OrderMessage(Long userId, Long goodsId,Long taskId) {
        this.userId = userId;
        this.goodsId = goodsId;
        this.taskId = taskId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "OrderMessage{" +
                "userId=" + userId +
                ", goodsId=" + goodsId +
                '}';
    }
}
