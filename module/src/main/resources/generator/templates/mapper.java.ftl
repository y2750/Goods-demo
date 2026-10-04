package ${package.Mapper};

import ${package.Entity}.${entity};
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * ${entity} 的五个基础数据库操作。
 * @author ${author}
 */
@Mapper
public interface ${table.mapperName} {

    @Select("SELECT ${selectColumns} FROM ${tableSql} WHERE `${pk.name}` = <#noparse>#{id}</#noparse> AND `${deleted.name}` = 0")
    ${entity} getById(@Param("id") ${pk.propertyType} id);

    @Select("SELECT ${selectColumns} FROM ${tableSql} WHERE `${deleted.name}` = 0 ORDER BY `${pk.name}`")
    List<${entity}> getAll();

    Integer insert(@Param("entity") ${entity} entity);

    Integer update(@Param("entity") ${entity} entity);

    @Update("UPDATE ${tableSql} SET `${deleted.name}` = 1, `${updated.name}` = <#noparse>#{time}</#noparse> WHERE `${pk.name}` = <#noparse>#{id}</#noparse> AND `${deleted.name}` = 0")
    Integer delete(@Param("id") ${pk.propertyType} id, @Param("time") ${updated.propertyType} time);
}
