package com.example.mall.main.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mall.main.domain.UserInfoVo;
import com.example.mall.main.domain.UserListFeedVo;
import com.example.mall.module.entity.User;
import com.example.mall.module.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author ny
 * @since 2026-10-03
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver exceptionResolver;

    @RequestMapping("/create")
    public Long userCreate(
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "password", required = false) String password,
            @RequestParam(name = "email", required = false) String email,
            HttpServletRequest request, HttpServletResponse response) {
        if (!StringUtils.hasText(username) || username.trim().length() > 50
                || !StringUtils.hasText(password) || password.length() > 100
                || (email != null && email.length() > 100)) {
            exceptionResolver.resolveException(request, response, null,
                    new IllegalArgumentException("Parameter error"));
            return null;
        }
        try {
            return service.edit(null, username.trim(), password, email);
        } catch (Exception e) {
            exceptionResolver.resolveException(request, response, null, e);
            return null;
        }
    }

    @RequestMapping("/update")
    public Long userUpdate(
            @RequestParam(name = "userId", required = false) Long userId,
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "password", required = false) String password,
            @RequestParam(name = "email", required = false) String email,
            HttpServletRequest request, HttpServletResponse response) {
        if (userId == null || userId <= 0
                || !StringUtils.hasText(username) || username.trim().length() > 50
                || (password != null && (!StringUtils.hasText(password) || password.length() > 100))
                || (email != null && email.length() > 100)) {
            exceptionResolver.resolveException(request, response, null,
                    new IllegalArgumentException("Parameter error"));
            return null;
        }
        try {
            return service.edit(userId, username.trim(), password, email);
        } catch (Exception e) {
            exceptionResolver.resolveException(request, response, null, e);
            return null;
        }
    }

    @RequestMapping("/delete")
    public String userDelete(
            @RequestParam(name = "userId", required = false) Long userId,
            HttpServletRequest request, HttpServletResponse response) {
        if (userId == null || userId <= 0) {
            exceptionResolver.resolveException(request, response, null,
                    new IllegalArgumentException("Parameter error"));
            return null;
        }
        try {
            return service.removeById(userId) ? "成功" : "失败";
        } catch (Exception e) {
            exceptionResolver.resolveException(request, response, null, e);
            return null;
        }
    }

    @RequestMapping("/info")
    public UserInfoVo userInfo(
            @RequestParam(name = "userId", required = false) Long userId,
            HttpServletRequest request, HttpServletResponse response) {
        if (userId == null || userId <= 0) {
            exceptionResolver.resolveException(request, response, null,
                    new IllegalArgumentException("Parameter error"));
            return null;
        }
        try {
            User user = service.getById(userId);
            if (user == null) {
                exceptionResolver.resolveException(request, response, null,
                        new IllegalArgumentException("User not found."));
                return null;
            }
            return UserInfoVo.from(user);
        } catch (Exception e) {
            exceptionResolver.resolveException(request, response, null, e);
            return null;
        }
    }

    @RequestMapping("/list")
    public UserListFeedVo userList(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "keyword", required = false) String keyword,
            HttpServletRequest request, HttpServletResponse response) {
        if (page <= 0) {
            exceptionResolver.resolveException(request, response, null,
                    new IllegalArgumentException("Parameter error"));
            return null;
        }
        try {
            Page<User> result = service.getUsersByPage(page, keyword);
            UserListFeedVo feed = new UserListFeedVo();
            feed.setList(result.getRecords().stream()
                    .map(UserInfoVo::from).collect(Collectors.toList()));
            feed.setTotal(result.getTotal());
            feed.setPageSize(UserService.PAGE_SIZE);
            return feed;
        } catch (Exception e) {
            exceptionResolver.resolveException(request, response, null, e);
            return null;
        }
    }
}
