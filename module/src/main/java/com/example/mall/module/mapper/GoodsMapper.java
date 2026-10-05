package com.example.mall.module.mapper;

import com.example.mall.module.entity.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * ClassName: GoodsMapper
 * Package: com.example.mall.module.mapper
 * Description:
 *
 * @Author
 * @Create 2026/9/26 14:48
 * @Version 1.0
 */
@Mapper
public interface GoodsMapper {
    @Select("SELECT g.*, c.name AS categoryName, c.image AS categoryImage "
            + "FROM goods g LEFT JOIN category c ON c.id = g.category_id AND c.is_deleted = 0 "
            + "WHERE g.id = #{id} AND g.is_deleted = 0")
    Goods getById(@Param("id") Integer id);

    @Select("select * from goods where is_deleted = 0")
    List<Goods> getAll();

    Integer update(@Param("goods") Goods goods);

    Integer insert(@Param("goods") Goods goods);

    @Update("update goods set is_deleted=1, update_time=#{time} where id=#{id} limit 1")
    int delete(@Param("id") Integer id, @Param("time") Integer time);

    List<Goods> getAppGoodsByPage(@Param("offset") int offset,
                                 @Param("pageSize") int pageSize,
                                 @Param("keyword") String keyword);

    List<Goods> getConsoleGoodsByPage(@Param("offset") int offset,
                                     @Param("pageSize") int pageSize,
                                     @Param("keyword") String keyword);

    Integer getConsoleTotal(@Param("keyword") String keyword);

    @Select("SELECT COUNT(*) FROM goods WHERE category_id = #{categoryId} AND is_deleted = 0")
    Integer countByCategoryId(@Param("categoryId") Integer categoryId);

    @Select("select * from goods WHERE id=#{id}")
    Goods extractById(Integer id);
}
