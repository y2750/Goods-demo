package com.example.mall.module.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author ny
 * @since 2026-10-03
 */
@Getter
@Setter
@ToString
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 创建时间，秒级时间戳
     */
    private Integer createTime;

    /**
     * 修改时间，秒级时间戳
     */
    private Integer updateTime;

    /**
     * 是否删除：0未删除，1已删除
     */
    @TableLogic
    private Byte isDeleted;
}
