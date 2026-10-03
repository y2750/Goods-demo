package com.example.mall.module.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.mall.module.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author ny
 * @since 2026-10-03
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
