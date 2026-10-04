package com.example.mall.console.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserInfoVo {
    private Long userId;
    private String username;
    private String email;
    private String createTime;
    private String updateTime;
}
