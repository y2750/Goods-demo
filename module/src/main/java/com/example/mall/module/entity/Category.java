package com.example.mall.module.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 商品分类
 *
 * @author ny
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Category {

    /** 分类ID */
    private Integer id;

    /** 分类名 */
    private String name;

    /** 分类图 */
    private String image;

    /** 创建时间，秒级时间戳 */
    private Integer createTime;

    /** 修改时间，秒级时间戳 */
    private Integer updateTime;

    /** 0正常，1已删除 */
    private Byte isDeleted;
}
