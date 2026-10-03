package com.example.mall.module.service;

import com.example.mall.module.entity.User;
import com.baomidou.mybatisplus.spring.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author ny
 * @since 2026-10-03
 */
public interface UserService extends IService<User> {

    int PAGE_SIZE = 10;

    Long edit(Long userId, String username, String password, String email);

    Page<User> getUsersByPage(Integer page, String keyword);
}
