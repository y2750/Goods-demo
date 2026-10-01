package com.example.mall.app.controller;

import com.example.mall.app.domain.GoodsListFeedVo;
import com.example.mall.app.domain.GoodsListVo;
import com.example.mall.app.domain.GoodsInfoVo;
import com.example.mall.module.entity.Goods;
import com.example.mall.module.service.GoodsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ClassName: GoodsController
 * Package: com.example.mall.app.controller
 * Description:
 *
 * @Author
 * @Create 2026/9/26 14:51
 * @Version 1.0
 */
@RestController
public class GoodsController {
    @Autowired
    private GoodsService service;

    @RequestMapping("/goods/info")
    public GoodsInfoVo goodsInfo(@RequestParam(name = "goodsId") Integer id) {
        Goods goods = service.getGoodsInfoById(id);
        GoodsInfoVo goodsInfoVO = new GoodsInfoVo();
        BeanUtils.copyProperties(goods, goodsInfoVO, "price");
        goodsInfoVO.setPrice(goods.getPrice().floatValue());
        List<String> list = Arrays.asList(goods.getImages().split("\\$"));
        goodsInfoVO.setGoodsImages(list);
        goodsInfoVO.setGoodsId(goods.getId());
        return goodsInfoVO;
    }

    @RequestMapping("/goods/list")
    public GoodsListFeedVo goodsList(@RequestParam(name = "page") Integer page) {
        List<Goods> goodsList = service.getGoodsByPage(page);
        List<GoodsListVo> goodsListVOList = new ArrayList<>();
        for (Goods goods : goodsList) {
            GoodsListVo goodsListVO = new GoodsListVo();
            BeanUtils.copyProperties(goods, goodsListVO, "price");
            goodsListVO.setPrice(goods.getPrice().floatValue());
            goodsListVO.setMainImages(goods.getImages().split("\\$")[0]);
            goodsListVO.setGoodsId(goods.getId());
            goodsListVOList.add(goodsListVO);
        }
        GoodsListFeedVo goodsListFeedVo = new GoodsListFeedVo();
        goodsListFeedVo.setList(goodsListVOList);
        goodsListFeedVo.setIsEnd(!(goodsList.size() == 10));
        return goodsListFeedVo;
    }
}
