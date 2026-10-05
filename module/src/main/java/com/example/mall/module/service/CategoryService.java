package com.example.mall.module.service;

import com.example.mall.module.entity.Category;
import com.example.mall.module.mapper.CategoryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 分类业务方法，直接调用 Mapper。
 * @author ny
 */
@Service
public class CategoryService {
    @Resource
    private CategoryMapper mapper;

    /** 列表关联查询允许分类缺失，由 Controller 决定是否跳过商品。 */
    public Category findById(Integer id) {
        return id == null ? null : mapper.getById(id);
    }

    /** 必须在调用方事务中使用，行锁持续到商品写入或分类删除完成。 */
    @Transactional(propagation = Propagation.MANDATORY)
    public Category getByIdForUpdate(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("categoryId 必须为正整数");
        }
        Category category = mapper.getByIdForUpdate(id);
        if (category == null) {
            throw new IllegalArgumentException("分类不存在或已删除：" + id);
        }
        return category;
    }

    public Category getById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("id 不能为空");
        }
        Category entity = mapper.getById(id);
        if (entity == null) {
            throw new IllegalArgumentException("Category 不存在或已删除：" + id);
        }
        return entity;
    }

    public List<Category> getAll() {
        return mapper.getAll();
    }

    /** keyword 为调用方组装好的 LIKE 条件，例如 %手机%。 */
    public List<Integer> getIdsByName(String keyword) {
        return mapper.getIdsByName(keyword);
    }

    /** id 为空时新增，有 id 时修改，成功返回分类 ID。 */
    public Integer edit(Integer categoryId, String name, String image) {
        Category entity = new Category();
        entity.setId(categoryId);
        entity.setName(name);
        entity.setImage(image);
        long timestamp = System.currentTimeMillis() / 1000;
        entity.setUpdateTime((int) timestamp);
        if (entity.getId() == null) {
            entity.setCreateTime((int) timestamp);
            entity.setIsDeleted((byte) 0);
            if (mapper.insert(entity) != 1 || entity.getId() == null) {
                throw new IllegalStateException("Category 新增失败");
            }
        } else if (mapper.update(entity) != 1) {
            throw new IllegalStateException("Category 修改失败，记录不存在或已删除");
        }
        return entity.getId();
    }

    public Integer delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("id 不能为空");
        }
        long timestamp = System.currentTimeMillis() / 1000;
        return mapper.delete(id, (int) timestamp);
    }
}
