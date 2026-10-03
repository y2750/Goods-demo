package com.example.mall.console.controller;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.mall.console.domain.GoodsInfoVo;
import com.example.mall.console.domain.GoodsListFeedVo;
import com.example.mall.console.domain.GoodsListVo;
import com.example.mall.module.entity.Goods;
import com.example.mall.module.service.GoodsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerExceptionResolver;

import static com.example.mall.module.service.GoodsService.PAGE_SIZE;

/**
 * ClassName: GoodsController
 * Package: com.example.mall.console.controller
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

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver exceptionResolver;

    @RequestMapping("/goods/create")
    public Integer goodsCreate(
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "price", required = false) Float price,
            @RequestParam(name = "description", required = false) String description,
            @RequestParam(name = "images", required = false) String images,
            HttpServletRequest request,
            HttpServletResponse response) {
        if (!StringUtils.hasText(title) || price == null) {
            exceptionResolver.resolveException(request, response, null,
                    new IllegalArgumentException("Parameter error"));
            return null;
        }
        title = title.trim();
        try {
            return service.edit(null, title, price, description, images);
        } catch (Exception e) {
            exceptionResolver.resolveException(request, response, null, e);
            return null;
        }
    }

    @RequestMapping("/goods/update")
    public Integer goodsUpdate(
            @RequestParam(name = "goodsId", required = false) Integer goodsId,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "price", required = false) Float price,
            @RequestParam(name = "description", required = false) String description,
            @RequestParam(name = "goodsImages", required = false) String goodsImages,
            HttpServletRequest request,
            HttpServletResponse response) {
        if (!StringUtils.hasText(title) || price == null || goodsId == null) {
            exceptionResolver.resolveException(request, response, null,
                    new IllegalArgumentException("Parameter error"));
            return null;
        }
        title = title.trim();
        try {
            return service.edit(goodsId, title, price, description, goodsImages);
        } catch (Exception e) {
            exceptionResolver.resolveException(request, response, null, e);
            return null;
        }
    }

    @RequestMapping("/goods/delete")
    public String goodsDelete(@RequestParam(name = "goodsId") Integer goodsId) {
        int result = service.deleteGoods(goodsId);
        return 1 == result ? "成功" : "失败";
    }

    @RequestMapping("/goods/info")
    public GoodsInfoVo goodsInfo(@RequestParam(name = "goodsId") Integer id) {
        Goods goods = service.getGoodsInfoById(id);
        GoodsInfoVo goodsInfoVO = new GoodsInfoVo();
        BeanUtils.copyProperties(goods, goodsInfoVO, "price");
        goodsInfoVO.setPrice(goods.getPrice().floatValue());
        List<String> list = Arrays.asList(goods.getImages().split("\\$"));
        goodsInfoVO.setGoodsImages(list);
        goodsInfoVO.setGoodsId(goods.getId());
        goodsInfoVO.setCreateTime(Instant.ofEpochSecond(goods.getCreateTime())
                .atZone(ZoneId.of("Asia/Shanghai"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        goodsInfoVO.setUpdateTime(Instant.ofEpochSecond(goods.getUpdateTime())
                .atZone(ZoneId.of("Asia/Shanghai"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return goodsInfoVO;
    }

    @RequestMapping("/goods/list")
    public GoodsListFeedVo goodsList(@RequestParam(name = "page") Integer page, @RequestParam(name = "keyword", required = false) String keyword) {
        List<Goods> goodsList = service.getGoodsByPage(page, keyword);
        List<GoodsListVo> goodsListVOList = new ArrayList<>();
        for (Goods goods : goodsList) {
            GoodsListVo goodsListVO = new GoodsListVo();
            BeanUtils.copyProperties(goods, goodsListVO, "price");
            goodsListVO.setPrice(goods.getPrice().floatValue());
            goodsListVO.setMainImage(goods.getImages().split("\\$")[0]);
            goodsListVO.setGoodsId(goods.getId());
            goodsListVOList.add(goodsListVO);
        }
        GoodsListFeedVo goodsListFeedVo = new GoodsListFeedVo();
        goodsListFeedVo.setList(goodsListVOList);
        goodsListFeedVo.setTotal(service.getTotal(keyword));
        goodsListFeedVo.setPageSize(PAGE_SIZE);
        return goodsListFeedVo;
    }
}
