package com.example.mall.module.mapper;

import com.example.mall.module.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * User 的五个基础数据库操作。
 * @author ny
 */
@Mapper
public interface UserMapper {

    @Select("SELECT `id` AS `id`, `username` AS `username`, `password` AS `password`, `email` AS `email`, `create_time` AS `createTime`, `update_time` AS `updateTime`, `is_deleted` AS `isDeleted` FROM `user` WHERE `id` = #{id} AND `is_deleted` = 0")
    User getById(@Param("id") Long id);

    @Select("SELECT `id` AS `id`, `username` AS `username`, `password` AS `password`, `email` AS `email`, `create_time` AS `createTime`, `update_time` AS `updateTime`, `is_deleted` AS `isDeleted` FROM `user` WHERE `is_deleted` = 0 ORDER BY `id`")
    List<User> getAll();

    Integer insert(@Param("entity") User entity);

    Integer update(@Param("entity") User entity);

    @Update("UPDATE `user` SET `is_deleted` = 1, `update_time` = #{time} WHERE `id` = #{id} AND `is_deleted` = 0")
    Integer delete(@Param("id") Long id, @Param("time") Integer time);

    @Select("<script>"
            + "SELECT id, username, email, create_time AS createTime, update_time AS updateTime "
            + "FROM `user` WHERE is_deleted = 0 "
            + "<if test='keyword != null'>AND username LIKE #{keyword} </if>"
            + "ORDER BY id LIMIT #{offset}, #{pageSize}"
            + "</script>")
    List<User> getUsersByPage(@Param("offset") long offset,
                             @Param("pageSize") int pageSize,
                             @Param("keyword") String keyword);

    @Select("<script>"
            + "SELECT COUNT(*) FROM `user` WHERE is_deleted = 0 "
            + "<if test='keyword != null'>AND username LIKE #{keyword}</if>"
            + "</script>")
    Long getTotal(@Param("keyword") String keyword);
}
