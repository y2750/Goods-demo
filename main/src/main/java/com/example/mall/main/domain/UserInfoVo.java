package com.example.mall.main.domain;

import com.example.mall.module.entity.User;
import lombok.Data;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Data
public class UserInfoVo {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Asia/Shanghai"));

    private Long userId;
    private String username;
    private String email;
    private String createTime;
    private String updateTime;

    public static UserInfoVo from(User user) {
        UserInfoVo vo = new UserInfoVo();
        vo.setUserId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setEmail(user.getEmail());
        if (user.getCreateTime() != null) {
            vo.setCreateTime(TIME_FORMAT.format(Instant.ofEpochSecond(user.getCreateTime())));
        }
        if (user.getUpdateTime() != null) {
            vo.setUpdateTime(TIME_FORMAT.format(Instant.ofEpochSecond(user.getUpdateTime())));
        }
        return vo;
    }
}
