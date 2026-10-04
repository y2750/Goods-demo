package com.example.mall.module.service;

import com.example.mall.module.entity.User;
import com.example.mall.module.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * User 的五个基础业务方法，直接调用 Mapper。
 * @author ny
 */
@Service
public class UserService {
    @Resource
    private UserMapper mapper;

    public User getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id 不能为空");
        }
        User entity = mapper.getById(id);
        if (entity == null) {
            throw new IllegalArgumentException("User 不存在或已删除：" + id);
        }
        return entity;
    }

    public List<User> getAll() {
        return mapper.getAll();
    }

    public Long insert(User entity) {
        if (entity == null || entity.getId() != null) {
            throw new IllegalArgumentException("新增时实体不能为空，且不能指定自增主键");
        }
        long timestamp = System.currentTimeMillis() / 1000;
        entity.setCreateTime((int) timestamp);
        entity.setUpdateTime((int) timestamp);
        entity.setIsDeleted((byte) 0);
        if (mapper.insert(entity) != 1 || entity.getId() == null) {
            throw new IllegalStateException("User 新增失败");
        }
        return entity.getId();
    }

    public Long update(User entity) {
        if (entity == null || entity.getId() == null) {
            throw new IllegalArgumentException("修改时实体和主键不能为空");
        }
        long timestamp = System.currentTimeMillis() / 1000;
        entity.setUpdateTime((int) timestamp);
        if (mapper.update(entity) != 1) {
            throw new IllegalStateException("User 修改失败，记录不存在或已删除");
        }
        return entity.getId();
    }

    public Integer delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id 不能为空");
        }
        long timestamp = System.currentTimeMillis() / 1000;
        return mapper.delete(id, (int) timestamp);
    }
}
