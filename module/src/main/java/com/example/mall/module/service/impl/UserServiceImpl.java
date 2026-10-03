package com.example.mall.module.service.impl;

import com.example.mall.module.entity.User;
import com.example.mall.module.mapper.UserMapper;
import com.example.mall.module.service.UserService;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author ny
 * @since 2026-10-03
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public Long edit(Long userId, String username, String password, String email) {
        User user = new User();
        int timestamp = (int) (System.currentTimeMillis() / 1000);
        user.setId(userId);
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setUpdateTime(timestamp);

        boolean success;
        if (userId == null) {
            user.setCreateTime(timestamp);
            user.setIsDeleted((byte) 0);
            success = save(user);
        } else {
            success = updateById(user);
        }
        if (!success) {
            throw new IllegalStateException(userId == null
                    ? "User creation failed." : "User update failed.");
        }
        return user.getId();
    }

    @Override
    public Page<User> getUsersByPage(Integer page, String keyword) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(User::getId, User::getUsername, User::getEmail,
                User::getCreateTime, User::getUpdateTime);
        if (StringUtils.hasText(keyword)) {
            wrapper.like(User::getUsername, keyword.trim());
        }
        wrapper.orderByAsc(User::getId);
        return page(new Page<>(page, PAGE_SIZE), wrapper);
    }
}
