package com.example.mall.module.mapper;

import com.example.mall.module.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Category 的五个基础数据库操作。
 * @author ny
 */
@Mapper
public interface CategoryMapper {

    @Select("SELECT `id` AS `id`, `name` AS `name`, `image` AS `image`, `create_time` AS `createTime`, `update_time` AS `updateTime`, `is_deleted` AS `isDeleted` FROM `category` WHERE `id` = #{id} AND `is_deleted` = 0")
    Category getById(@Param("id") Integer id);

    @Select("SELECT id, name, image, create_time AS createTime, update_time AS updateTime, "
            + "is_deleted AS isDeleted FROM category WHERE id = #{id} AND is_deleted = 0 FOR UPDATE")
    Category getByIdForUpdate(@Param("id") Integer id);

    @Select("SELECT `id` AS `id`, `name` AS `name`, `image` AS `image`, `create_time` AS `createTime`, `update_time` AS `updateTime`, `is_deleted` AS `isDeleted` FROM `category` WHERE `is_deleted` = 0 ORDER BY `id`")
    List<Category> getAll();

    @Select("SELECT id FROM category WHERE is_deleted = 0 AND name LIKE #{keyword} ORDER BY id")
    List<Integer> getIdsByName(@Param("keyword") String keyword);

    Integer insert(@Param("entity") Category entity);

    Integer update(@Param("entity") Category entity);

    @Update("UPDATE `category` SET `is_deleted` = 1, `update_time` = #{time} WHERE `id` = #{id} AND `is_deleted` = 0")
    Integer delete(@Param("id") Integer id, @Param("time") Integer time);
}
