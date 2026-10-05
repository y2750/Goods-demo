package com.example.mall.app.controller;

import com.example.mall.app.exception.GlobalExceptionHandler;
import com.example.mall.app.domain.GoodsInfoVo;
import com.example.mall.app.domain.GoodsListFeedVo;
import com.example.mall.app.domain.GoodsListVo;
import com.example.mall.module.entity.Goods;
import com.example.mall.module.service.GoodsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.example.mall.module.service.GoodsService.PAGE_SIZE;

@RestController
public class GoodsController {
    @Autowired
    private GlobalExceptionHandler exceptionHandler;

    @Autowired
    private GoodsService service;

    @RequestMapping("/goods/info")
    public GoodsInfoVo goodsInfo(@RequestParam(name = "goodsId") Integer id) {
        try {
            Goods goods = service.getGoodsInfoById(id);
            if (goods == null) {
                exceptionHandler.resolveException(new IllegalArgumentException("商品不存在或已删除：" + id));
                return null;
            }
            // 分类名为 NOT NULL；联表结果为空表示分类不存在或已删除。
            if (goods.getCategoryName() == null) {
                exceptionHandler.resolveException(new IllegalArgumentException("分类不存在或已删除"));
                return null;
            }
            GoodsInfoVo vo = new GoodsInfoVo();
            BeanUtils.copyProperties(goods, vo);
            vo.setGoodsId(goods.getId());
            vo.setGoodsImages(goods.getImages() == null ? Collections.emptyList()
                    : Arrays.asList(goods.getImages().split("\\$")));
            return vo;
        } catch (Exception e) {
            exceptionHandler.resolveException(e);
            return null;
        }
    }

    @RequestMapping("/goods/list")
    public GoodsListFeedVo goodsList(@RequestParam(name = "page") Integer page,
                                    @RequestParam(name = "keyword", required = false) String keyword) {
        try {
            if (page <= 0) {
                exceptionHandler.resolveException(new IllegalArgumentException("page 必须为正整数"));
                return null;
            }
            List<Goods> goodsList = service.getAppGoodsByPage(page, keyword);
            GoodsListFeedVo feed = new GoodsListFeedVo();
            // 沿用原始分页规则，先判断 isEnd，不能用过滤后的列表长度判断。
            feed.setIsEnd(goodsList.size() < PAGE_SIZE);
            List<GoodsListVo> list = new ArrayList<>();
            for (Goods goods : goodsList) {
                // 分类信息已由联表查询取出，不再逐条查询分类。
                if (goods.getCategoryName() == null) {
                    continue;
                }
                GoodsListVo vo = new GoodsListVo();
                BeanUtils.copyProperties(goods, vo);
                vo.setGoodsId(goods.getId());
                String images = goods.getImages();
                vo.setMainImage(images == null ? "" : images.split("\\$", -1)[0]);
                list.add(vo);
            }
            feed.setList(list);
            return feed;
        } catch (Exception e) {
            exceptionHandler.resolveException(e);
            return null;
        }
    }
}
