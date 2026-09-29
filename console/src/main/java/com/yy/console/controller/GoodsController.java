package com.yy.console.controller;


import com.yy.module.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

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

    @RequestMapping("/goods/create")
    public String goodsCreate(
            @RequestParam(name = "title") String title,
            @RequestParam(name = "price") Double price,
            @RequestParam(name = "description") String description,
            @RequestParam(name = "goodsImages") String images) {

        int result = service.createGoods(title, price, description, images);
        return 1 == result ? "成功" : "失败";
    }

    @RequestMapping("/goods/update")
    public String goodsUpdate(@RequestParam(name = "goodsId") BigInteger goodsId,
                              @RequestParam(name = "title") String title,
                              @RequestParam(name = "price") Double price,
                              @RequestParam(name = "description") String description,
                              @RequestParam(name = "images") String images) {
        int result = service.updateGoods(goodsId, title, price, description, images);
        return 1 == result ? "成功" : "失败";
    }

    @RequestMapping("/goods/delete")
    public String goodsDelete(@RequestParam(name = "goodsId") BigInteger goodsId) {
        int result = service.deleteGoods(goodsId);
        return 1 == result ? "成功" : "失败";
    }
}
