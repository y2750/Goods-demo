package com.yy.module.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigInteger;

/**
 * ClassName: Student
 * Package: com.example.demo.entity
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
    private BigInteger id;
    private String title;
    private String images;
    private Double price;
    private String description;
    private Integer createTime;
    private Integer updateTime;
    private Integer isDeleted;
}
