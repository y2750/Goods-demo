package com.example.mall.app.controller;

import com.example.mall.app.domain.UserInfoVo;
import com.example.mall.app.domain.UserListFeedVo;
import com.example.mall.module.entity.User;
import com.example.mall.module.service.UserService;
import org.springframework.beans.BeanUtils;
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

import static com.example.mall.module.service.UserService.PAGE_SIZE;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService service;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver exceptionResolver;

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
            UserInfoVo vo = new UserInfoVo();
            BeanUtils.copyProperties(user, vo);
            vo.setUserId(user.getId());
            return vo;
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
            exceptionResolver.resolveException(request, response, null, e);
            return null;
        }
    }
}
