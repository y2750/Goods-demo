package com.yy.app.controller;

import com.yy.app.domain.GoodsListFeedVo;
import com.yy.app.domain.GoodsListVo;
import com.yy.app.domain.GoodsInfoVo;
import com.yy.module.entity.Goods;
import com.yy.module.service.GoodsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.*;


/**
 * ClassName: StudentController
 * Package: com.example.demo.controller
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
    public GoodsInfoVo goodsInfo(@RequestParam(name = "goodsId") BigInteger id) {
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
        BeanUtils.copyProperties(goods, goodsListVO);
        goodsListVO.setMainImages(goods.getImages().split("\\$")[0]);
        goodsListVO.setGoodsId(goods.getId());
        return goodsListVO;
    }

    private static GoodsInfoVo getGoodsVO(Goods goods) {
        GoodsInfoVo goodsInfoVO = new GoodsInfoVo();
        BeanUtils.copyProperties(goods, goodsInfoVO);
        List<String> list = Arrays.asList(goods.getImages().split("\\$"));
        goodsInfoVO.setGoodsImages(list);
        goodsInfoVO.setGoodsId(goods.getId());
        return goodsInfoVO;
    }
}
