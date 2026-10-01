package com.example.mall.console.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * ClassName: GoodsListFeedVo
 * Package: com.example.mall.app.domain
 * Description:
 *
 * @Author
 * @Create 2026/9/29 13:34
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
public class GoodsListFeedVo {
    private List<GoodsListVo> list;
    private Integer total;
    private Integer pageSize;
}
