package com.yy.module.mapper;


import com.yy.module.entity.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigInteger;
import java.util.List;

/**
 * ClassName: StudentMapper
 * Package: com.example.demo.mapper
 * Description:
 *
 * @Author
 * @Create 2026/9/26 14:48
 * @Version 1.0
 */
@Mapper
public interface GoodsMapper {

    @Select("select * from goods WHERE id=#{id} and is_deleted = 0")
    Goods getById(@Param("id")BigInteger id);
    @Select("select * from goods where is_deleted = 0")
    List<Goods> getAll();

/**
 * 更新学生信息的方法
 * @param goods 包含更新后学生信息的Student对象
 * @return 受影响的行数，通常用于判断更新是否成功
 */
    int update(@Param("goods") Goods goods);

    int insert(@Param("goods") Goods goods);

    @Update("update goods set is_deleted=1, update_time=#{time} where id=#{id} limit 1")
    int delete(@Param("id") BigInteger id, @Param("time") Integer time);
}
