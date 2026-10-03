package com.example.mall.module.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mall.module.entity.Goods;
import com.example.mall.module.mapper.GoodsMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

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

    public static final int PAGE_SIZE = 10;

    public Integer edit(Integer goodsId, String title, Float price, String description, String images) {
        Goods goods = new Goods();
        int timestamp = (int) (System.currentTimeMillis() / 1000);
        goods.setId(goodsId);
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
            result = mapper.updateById(goods);
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
        return mapper.deleteById(goodsId);
    }

    public Goods getGoodsInfoById(Integer id) {
        return mapper.selectById(id);
    }

    public Page<Goods> getGoodsByPage(Integer pageNo, String keyword) {

        Page<Goods> page = new Page<>(pageNo, PAGE_SIZE);
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Goods::getTitle, keyword.trim());
        }
        wrapper.orderByAsc(Goods::getId);
        Page<Goods> result = mapper.selectPage(page, wrapper);
        return result;
    }
}
