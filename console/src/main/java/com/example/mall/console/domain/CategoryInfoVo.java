package com.example.mall.console.domain;

import lombok.Data;

@Data
public class CategoryInfoVo {
    private Integer categoryId;
    private String name;
    private String image;
    private String createTime;
    private String updateTime;
}
