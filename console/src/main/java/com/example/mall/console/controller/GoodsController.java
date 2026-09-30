package com.example.mall.console.controller;

import java.math.BigDecimal;
import com.example.mall.module.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping("/goods/create")
    public Integer goodsCreate(
            @RequestParam(name = "title") String title,
            @RequestParam(name = "price") BigDecimal price,
            @RequestParam(name = "description") String description,
            @RequestParam(name = "goodsImages") String images) {
        return service.createGoods(title, price, description, images);
    }

    @RequestMapping("/goods/update")
    public String goodsUpdate(@RequestParam(name = "goodsId") Integer goodsId,
                              @RequestParam(name = "title") String title,
                              @RequestParam(name = "price") BigDecimal price,
                              @RequestParam(name = "description") String description,
                              @RequestParam(name = "images") String images) {
        int result = service.updateGoods(goodsId, title, price, description, images);
        return 1 == result ? "成功" : "失败";
    }

    @RequestMapping("/goods/delete")
    public String goodsDelete(@RequestParam(name = "goodsId") Integer goodsId) {
        int result = service.deleteGoods(goodsId);
        return 1 == result ? "成功" : "失败";
    }
}
