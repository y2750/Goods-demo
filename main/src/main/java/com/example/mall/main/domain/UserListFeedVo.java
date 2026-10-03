package com.example.mall.main.domain;

import lombok.Data;
import java.util.List;

@Data
public class UserListFeedVo {
    private List<UserInfoVo> list;
    private Long total;
    private Integer pageSize;
}
