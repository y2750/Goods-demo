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
        return getGoodsVO(goods);
    }

    @RequestMapping("/goods/list")
    public GoodsListFeedVo goodsList() {
        List<Goods> goodsList = service.getAllGoods();
        List<GoodsListVo> goodsListVOList = new ArrayList<>();
        for (Goods goods : goodsList) {
            goodsListVOList.add(getGoodsListVO(goods));
        }
        GoodsListFeedVo goodsListFeedVo = new GoodsListFeedVo();
        goodsListFeedVo.setList(goodsListVOList);
        return goodsListFeedVo;
    }

    private GoodsListVo getGoodsListVO(Goods goods) {
        GoodsListVo goodsListVO = new GoodsListVo();
        BeanUtils.copyProperties(goods, goodsListVO, "price");
        goodsListVO.setPrice(goods.getPrice().floatValue());
        goodsListVO.setMainImages(goods.getImages().split("\\$")[0]);
        goodsListVO.setGoodsId(goods.getId());
        return goodsListVO;
    }

    private static GoodsInfoVo getGoodsVO(Goods goods) {
        GoodsInfoVo goodsInfoVO = new GoodsInfoVo();
        BeanUtils.copyProperties(goods, goodsInfoVO, "price");
        goodsInfoVO.setPrice(goods.getPrice().floatValue());
        List<String> list = Arrays.asList(goods.getImages().split("\\$"));
        goodsInfoVO.setGoodsImages(list);
        goodsInfoVO.setGoodsId(goods.getId());
        return goodsInfoVO;
    }
}
