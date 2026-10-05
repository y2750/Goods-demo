package com.example.mall.app.controller;

import com.example.mall.app.exception.GlobalExceptionHandler;
import com.example.mall.app.domain.CategoryListVo;
import com.example.mall.module.entity.Category;
import com.example.mall.module.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private GlobalExceptionHandler exceptionHandler;

    @Autowired
    private CategoryService service;

    /** 不分页，只返回未删除分类。 */
    @RequestMapping("/list")
    public List<CategoryListVo> categoryList() {
        try {
            List<CategoryListVo> list = new ArrayList<>();
            for (Category category : service.getAll()) {
                CategoryListVo vo = new CategoryListVo();
                vo.setCategoryId(category.getId());
                vo.setName(category.getName());
                vo.setImage(category.getImage());
                list.add(vo);
            }
            return list;
        } catch (Exception e) {
            exceptionHandler.resolveException(e);
            return null;
        }
    }
}
