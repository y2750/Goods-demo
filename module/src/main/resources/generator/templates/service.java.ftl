package ${package.Service};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * ${entity} 的五个基础业务方法，直接调用 Mapper。
 * @author ${author}
 */
@Service
public class ${table.serviceName} {
    @Resource
    private ${table.mapperName} mapper;

    public ${entity} getById(${pk.propertyType} id) {
        if (id == null) {
            throw new IllegalArgumentException("id 不能为空");
        }
        ${entity} entity = mapper.getById(id);
        if (entity == null) {
            throw new IllegalArgumentException("${entity} 不存在或已删除：" + id);
        }
        return entity;
    }

    public List<${entity}> getAll() {
        return mapper.getAll();
    }

    public ${pk.propertyType} insert(${entity} entity) {
        if (entity == null || entity.get${pk.capitalName}() != null) {
            throw new IllegalArgumentException("新增时实体不能为空，且不能指定自增主键");
        }
        long timestamp = System.currentTimeMillis() / 1000;
        entity.set${created.capitalName}(${createdValue});
        entity.set${updated.capitalName}(${updatedValue});
        entity.set${deleted.capitalName}(${deletedZero});
        if (mapper.insert(entity) != 1 || entity.get${pk.capitalName}() == null) {
            throw new IllegalStateException("${entity} 新增失败");
        }
        return entity.get${pk.capitalName}();
    }

    public ${pk.propertyType} update(${entity} entity) {
        if (entity == null || entity.get${pk.capitalName}() == null) {
            throw new IllegalArgumentException("修改时实体和主键不能为空");
        }
        long timestamp = System.currentTimeMillis() / 1000;
        entity.set${updated.capitalName}(${updatedValue});
        if (mapper.update(entity) != 1) {
            throw new IllegalStateException("${entity} 修改失败，记录不存在或已删除");
        }
        return entity.get${pk.capitalName}();
    }

    public Integer delete(${pk.propertyType} id) {
        if (id == null) {
            throw new IllegalArgumentException("id 不能为空");
        }
        long timestamp = System.currentTimeMillis() / 1000;
        return mapper.delete(id, ${updatedValue});
    }
}
