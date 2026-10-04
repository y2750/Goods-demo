package com.example.mall.console.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import java.util.List;

@Data
@Accessors(chain = true)
public class UserListFeedVo {
    private List<UserInfoVo> list;
    private Long total;
    private Integer pageSize;
}
