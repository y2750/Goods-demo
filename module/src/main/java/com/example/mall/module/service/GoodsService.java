package com.example.mall.module.service;

import com.example.mall.module.entity.Goods;
import com.example.mall.module.mapper.GoodsMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * ClassName: GoodsService
 * Package: com.example.mall.module.service
 * Description:
 *
 * @Author
 * @Create 2026/9/26 14:50
 * @Version 1.0
 */
@Service
public class GoodsService {
    @Resource
    private GoodsMapper mapper;

    @Resource
    private CategoryService categoryService;

    public static final int PAGE_SIZE = 10;

    @Transactional(rollbackFor = Exception.class)
    public Integer edit(Integer goodsId, String title, Float price, String description, String images,
                        Integer categoryId) {
        // 与删除分类使用同一行锁，保证校验到商品写入期间分类不会被删除。
        categoryService.getByIdForUpdate(categoryId);
        Goods goods = new Goods();
        int timestamp = (int) (System.currentTimeMillis() / 1000);
        goods.setId(goodsId);
        goods.setCategoryId(categoryId);
        goods.setTitle(title);
        goods.setPrice(price);
        goods.setDescription(description);
        goods.setImages(images);
        goods.setUpdateTime(timestamp);
        int result;
        if (goods.getId() == null) {
            goods.setCreateTime(timestamp);
            result = insert(goods);
            if (result == 0) {
                throw new RuntimeException("Goods creation failed.");
            }
            return goods.getId();
        } else {
            Goods byId = mapper.extractById(goods.getId());
            if (byId == null) {
                throw new RuntimeException("Goods ID does not exist.");
            }
            result = mapper.update(goods);
            if (result == 0) {
                throw new RuntimeException("Goods update failed.");
            }
            return goods.getId();
        }
    }

    private Integer insert(Goods goods) {
        return mapper.insert(goods);
    }

    public int deleteGoods(Integer goodsId) {
        return mapper.delete(goodsId, (int) (System.currentTimeMillis() / 1000));
    }

    public Goods getGoodsInfoById(Integer id) {
        return mapper.getById(id);
    }

    public List<Goods> getAppGoodsByPage(Integer page, String keyword) {
        int offset = (page - 1) * PAGE_SIZE;
        String pattern = toKeywordPattern(keyword);
        // 把分类子查询拆开：先查分类 ID，再用标题 OR 分类 ID 查询商品并分页。
        List<Integer> categoryIds = pattern == null ? Collections.emptyList()
                : categoryService.getIdsByName(pattern);
        return mapper.getAppGoodsByPage(offset, PAGE_SIZE, pattern, categoryIds);
    }

    public List<Goods> getConsoleGoodsByPage(Integer page, String keyword) {
        int offset = (page - 1) * PAGE_SIZE;
        return mapper.getConsoleGoodsByPage(offset, PAGE_SIZE, toKeywordPattern(keyword));
    }

    public Integer getConsoleTotal(String keyword) {
        return mapper.getConsoleTotal(toKeywordPattern(keyword));
    }

    public boolean hasGoodsInCategory(Integer categoryId) {
        return mapper.countByCategoryId(categoryId) > 0;
    }

    private String toKeywordPattern(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return null;
        }
        return "%" + keyword.trim() + "%";
    }
}
