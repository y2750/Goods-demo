package com.example.mall.module.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用户表
 *
 * @author ny
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class User {

    /** 用户ID */
    private Long id;

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;

    /** 邮箱 */
    private String email;

    /** 创建时间，秒级时间戳 */
    private Integer createTime;

    /** 修改时间，秒级时间戳 */
    private Integer updateTime;

    /** 是否删除：0未删除，1已删除 */
    private Byte isDeleted;
}
