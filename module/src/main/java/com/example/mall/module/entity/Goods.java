package com.example.mall.module.entity;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * ClassName: Goods
 * Package: com.example.mall.module.entity
 * Description:
 *
 * @Author
 * @Create 2026/9/26 14:46
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Goods {
    private Integer id;
    private String title;
    private String images;
    private Float price;
    private String description;
    private Integer createTime;
    private Integer updateTime;
    private Integer isDeleted;
}
