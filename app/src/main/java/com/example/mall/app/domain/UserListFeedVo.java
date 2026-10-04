package com.example.mall.app.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import java.util.List;

@Data
@Accessors(chain = true)
public class UserListFeedVo {
    private List<UserInfoVo> list;
    private Boolean isEnd;
}
