package com.example.mall.console.controller;

import com.example.mall.module.entity.User;
import com.example.mall.module.service.UserService;
import com.example.mall.console.domain.UserInfoVo;
import com.example.mall.console.domain.UserListFeedVo;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static com.example.mall.module.service.UserService.PAGE_SIZE;

/**
 * 用户管理接口。
 * @author ny
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Asia/Shanghai"));

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
            User entity = new User();
            entity.setUsername(username.trim());
            entity.setPassword(password);
            entity.setEmail(email);
            return service.insert(entity);
        } catch (Exception e) {
            exceptionResolver.resolveException(request, response, null, e);
            return null;
        }
    }

    @RequestMapping("/update")
    public Long userUpdate(
            @RequestParam(name = "userId", required = false) Long id,
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "password", required = false) String password,
            @RequestParam(name = "email", required = false) String email,
            HttpServletRequest request, HttpServletResponse response) {
        if (id == null || id <= 0
                || !StringUtils.hasText(username) || username.trim().length() > 50
                || (password != null && (!StringUtils.hasText(password) || password.length() > 100))
                || (email != null && email.length() > 100)) {
            exceptionResolver.resolveException(request, response, null,
                    new IllegalArgumentException("Parameter error"));
            return null;
        }
        try {
            User entity = new User();
            entity.setId(id);
            entity.setUsername(username.trim());
            entity.setPassword(password);
            entity.setEmail(email);
            return service.update(entity);
        } catch (Exception e) {
            exceptionResolver.resolveException(request, response, null, e);
            return null;
        }
    }

    @RequestMapping("/delete")
    public String userDelete(
            @RequestParam(name = "userId", required = false) Long id,
            HttpServletRequest request, HttpServletResponse response) {
        if (id == null || id <= 0) {
            exceptionResolver.resolveException(request, response, null,
                    new IllegalArgumentException("userId 必须为正整数"));
            return null;
        }
        try {
            return service.delete(id) == 1 ? "成功" : "失败";
        } catch (Exception e) {
            exceptionResolver.resolveException(request, response, null, e);
            return null;
        }
    }

    @RequestMapping("/info")
    public UserInfoVo userInfo(
            @RequestParam(name = "userId", required = false) Long id,
            HttpServletRequest request, HttpServletResponse response) {
        if (id == null || id <= 0) {
            exceptionResolver.resolveException(request, response, null,
                    new IllegalArgumentException("userId 必须为正整数"));
            return null;
        }
        try {
            return toInfoVo(service.getById(id));
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
            List<UserInfoVo> list = new ArrayList<>();
            for (User user : service.getUsersByPage(page, keyword)) {
                list.add(toInfoVo(user));
            }
            UserListFeedVo feed = new UserListFeedVo();
            feed.setList(list);
            feed.setTotal(service.getTotal(keyword));
            feed.setPageSize(PAGE_SIZE);
            return feed;
        } catch (Exception e) {
            exceptionResolver.resolveException(request, response, null, e);
            return null;
        }
    }

    private UserInfoVo toInfoVo(User user) {
        UserInfoVo vo = new UserInfoVo();
        BeanUtils.copyProperties(user, vo, "createTime", "updateTime");
        vo.setUserId(user.getId());
        if (user.getCreateTime() != null) {
            vo.setCreateTime(TIME_FORMAT.format(Instant.ofEpochSecond(user.getCreateTime())));
        }
        if (user.getUpdateTime() != null) {
            vo.setUpdateTime(TIME_FORMAT.format(Instant.ofEpochSecond(user.getUpdateTime())));
        }
        return vo;
    }
}
