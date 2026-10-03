package com.example.mall.module.service;

import com.example.mall.module.entity.Goods;
import com.example.mall.module.mapper.GoodsMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    public List<Goods> getGoodsByPage(Integer page,String keyword) {
        int offset = (page - 1) * PAGE_SIZE;
        return mapper.getGoodsByPage(offset, PAGE_SIZE, toTitlePattern(keyword));
    }

    public Integer getTotal(String keyword) {
        return mapper.getTotal(toTitlePattern(keyword));
    }

    private String toTitlePattern(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return null;
        }
        return "%" + keyword.trim() + "%";
    }
}
