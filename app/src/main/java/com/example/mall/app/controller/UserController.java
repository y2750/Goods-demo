package com.example.mall.app.controller;

import com.example.mall.app.exception.GlobalExceptionHandler;
import com.example.mall.app.domain.UserInfoVo;
import com.example.mall.app.domain.UserListFeedVo;
import com.example.mall.module.entity.User;
import com.example.mall.module.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.example.mall.module.service.UserService.PAGE_SIZE;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private GlobalExceptionHandler exceptionHandler;

    @Autowired
    private UserService service;

    @RequestMapping("/info")
    public UserInfoVo userInfo(
            @RequestParam(name = "userId", required = false) Long userId) {
        try {
            if (userId == null || userId <= 0) {
                exceptionHandler.resolveException(new IllegalArgumentException("Parameter error"));
                return null;
            }
            User user = service.getById(userId);
            UserInfoVo vo = new UserInfoVo();
            BeanUtils.copyProperties(user, vo);
            vo.setUserId(user.getId());
            return vo;
        } catch (Exception e) {
            exceptionHandler.resolveException(e);
            return null;
        }
    }

    @RequestMapping("/list")
    public UserListFeedVo userList(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "keyword", required = false) String keyword) {
        try {
            if (page <= 0) {
                exceptionHandler.resolveException(new IllegalArgumentException("Parameter error"));
                return null;
            }
            List<UserInfoVo> list = new ArrayList<>();
            for (User user : service.getUsersByPage(page, keyword)) {
                UserInfoVo vo = new UserInfoVo();
                BeanUtils.copyProperties(user, vo);
                vo.setUserId(user.getId());
                list.add(vo);
            }
            UserListFeedVo feed = new UserListFeedVo();
            feed.setList(list);
            feed.setIsEnd((long) page * PAGE_SIZE >= service.getTotal(keyword));
            return feed;
        } catch (Exception e) {
            exceptionHandler.resolveException(e);
            return null;
        }
    }
}
