package com.example.seckill.entity;

public class Goods {
    private Long id;        // 商品 id
    private String name;    // 商品名字
    //private Integer stock;  // 库存

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // getter / setter（IDEA 可以自动生成）
}
