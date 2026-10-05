package com.example.mall.console.controller;

import com.example.mall.console.exception.GlobalExceptionHandler;
import com.example.mall.console.domain.CategoryInfoVo;
import com.example.mall.module.entity.Category;
import com.example.mall.module.service.CategoryService;
import com.example.mall.module.service.GoodsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CategoryController {
    @Autowired
    private GlobalExceptionHandler exceptionHandler;

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Asia/Shanghai"));

    @Autowired
    private CategoryService service;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @RequestMapping("/category/create")
    public Integer categoryCreate(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "image", required = false) String image) {
        if (!StringUtils.hasText(name) || name.trim().length() > 50
                || image == null || image.length() > 255) {
            exceptionHandler.resolveException(
                    new IllegalArgumentException("分类名必填且不超过50字，分类图必传且不超过255字符"));
            return null;
        }
        name = name.trim();
        try {
            return service.edit(null, name, image);
        } catch (Exception e) {
            exceptionHandler.resolveException(e);
            return null;
        }
    }

    @RequestMapping("/category/update")
    public Integer categoryUpdate(
            @RequestParam(name = "categoryId", required = false) Integer categoryId,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "image", required = false) String image) {
        if (categoryId == null || categoryId <= 0
                || !StringUtils.hasText(name) || name.trim().length() > 50
                || image == null || image.length() > 255) {
            exceptionHandler.resolveException(
                    new IllegalArgumentException("分类参数错误：categoryId 必须为正整数，分类名和分类图必传"));
            return null;
        }
        name = name.trim();
        try {
            return service.edit(categoryId, name, image);
        } catch (Exception e) {
            exceptionHandler.resolveException(e);
            return null;
        }
    }

    @RequestMapping("/category/delete")
    public String categoryDelete(
            @RequestParam(name = "categoryId", required = false) Integer categoryId) {
        if (categoryId == null || categoryId <= 0) {
            exceptionHandler.resolveException(new IllegalArgumentException("categoryId 必须为正整数"));
            return null;
        }
        try {
            // 检查商品与删除分类放在同一事务中，事务结束后再处理错误响应。
            String result = transactionTemplate.execute(status -> {
                service.getByIdForUpdate(categoryId);
                if (goodsService.hasGoodsInCategory(categoryId)) {
                    status.setRollbackOnly();
                    return null;
                }
                return service.delete(categoryId) == 1 ? "成功" : "失败";
            });
            if (result == null) {
                exceptionHandler.resolveException(
                        new IllegalStateException("该分类下还有未删除商品，不允许删除分类"));
            }
            return result;
        } catch (Exception e) {
            exceptionHandler.resolveException(e);
            return null;
        }
    }

    @RequestMapping("/category/info")
    public CategoryInfoVo categoryInfo(
            @RequestParam(name = "categoryId", required = false) Integer categoryId) {
        if (categoryId == null || categoryId <= 0) {
            exceptionHandler.resolveException(new IllegalArgumentException("categoryId 必须为正整数"));
            return null;
        }
        try {
            Category category = service.getById(categoryId);
            CategoryInfoVo vo = new CategoryInfoVo();
            BeanUtils.copyProperties(category, vo, "createTime", "updateTime");
            vo.setCategoryId(category.getId());
            vo.setCreateTime(TIME_FORMAT.format(Instant.ofEpochSecond(category.getCreateTime())));
            vo.setUpdateTime(TIME_FORMAT.format(Instant.ofEpochSecond(category.getUpdateTime())));
            return vo;
        } catch (Exception e) {
            exceptionHandler.resolveException(e);
            return null;
        }
    }

    @RequestMapping("/category/list")
    public List<CategoryInfoVo> categoryList() {
        try {
            List<CategoryInfoVo> list = new ArrayList<>();
            for (Category category : service.getAll()) {
                CategoryInfoVo vo = new CategoryInfoVo();
                BeanUtils.copyProperties(category, vo, "createTime", "updateTime");
                vo.setCategoryId(category.getId());
                vo.setCreateTime(TIME_FORMAT.format(Instant.ofEpochSecond(category.getCreateTime())));
                vo.setUpdateTime(TIME_FORMAT.format(Instant.ofEpochSecond(category.getUpdateTime())));
                list.add(vo);
            }
            return list;
        } catch (Exception e) {
            exceptionHandler.resolveException(e);
            return null;
        }
    }

}
