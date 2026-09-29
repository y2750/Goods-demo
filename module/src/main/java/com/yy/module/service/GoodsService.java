package com.yy.module.service;



import com.yy.module.entity.Goods;
import com.yy.module.mapper.GoodsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

/**
 * ClassName: StudentService
 * Package: com.example.demo.service
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

    public int createGoods(String title, Double price, String description, String images) {
        Goods goods= new Goods();
        int timestamp = (int) (System.currentTimeMillis() / 1000);
        goods.setTitle(title);
        goods.setPrice(price);
        goods.setDescription(description);
        goods.setImages(images);
        goods.setCreateTime(timestamp);
        goods.setUpdateTime(timestamp);
        return mapper.insert(goods);
    }

    public int updateGoods(BigInteger goodsId, String title, Double price, String description, String images) {
        Goods goods= new Goods();
        int timestamp = (int) (System.currentTimeMillis() / 1000);
        goods.setId(goodsId);
        goods.setTitle(title);
        goods.setPrice(price);
        goods.setDescription(description);
        goods.setImages(images);
        goods.setUpdateTime(timestamp);
        return mapper.update(goods);
    }

    public int deleteGoods(BigInteger goodsId) {
        return mapper.delete(goodsId, (int) (System.currentTimeMillis() / 1000));
    }

    public Goods getGoodsInfoById(BigInteger id) {
        return mapper.getById(id);
    }
    public List<Goods> getAllGoods() {
        return mapper.getAll();
    }
}
