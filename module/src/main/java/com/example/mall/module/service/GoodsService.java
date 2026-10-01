package com.example.mall.module.service;

import java.math.BigDecimal;

import com.example.mall.module.entity.Goods;
import com.example.mall.module.mapper.GoodsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    public static final int PAGE_SIZE = 10;

    public Integer createGoods(String title, BigDecimal price, String description, String images) {
        Goods goods = new Goods();
        int timestamp = (int) (System.currentTimeMillis() / 1000);
        goods.setTitle(title);
        goods.setPrice(price);
        goods.setDescription(description);
        goods.setImages(images);
        goods.setCreateTime(timestamp);
        goods.setUpdateTime(timestamp);
        mapper.insert(goods);
        return goods.getId();
    }

    public int updateGoods(Integer goodsId, String title, BigDecimal price, String description, String images) {
        Goods goods = new Goods();
        int timestamp = (int) (System.currentTimeMillis() / 1000);
        goods.setId(goodsId);
        goods.setTitle(title);
        goods.setPrice(price);
        goods.setDescription(description);
        goods.setImages(images);
        goods.setUpdateTime(timestamp);
        return mapper.update(goods);
    }

    public int deleteGoods(Integer goodsId) {
        return mapper.delete(goodsId, (int) (System.currentTimeMillis() / 1000));
    }

    public Goods getGoodsInfoById(Integer id) {
        return mapper.getById(id);
    }

    public List<Goods> getGoodsByPage(Integer page) {
        int offset = (page - 1) * PAGE_SIZE;
        return mapper.getGoodsByPage(offset, PAGE_SIZE);
    }

    public Integer getTotal() {
        return mapper.getTotal();
    }
}
