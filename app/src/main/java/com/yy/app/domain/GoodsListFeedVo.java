package com.yy.app.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * ClassName: GoodsListFeedVo
 * Package: com.yy.app.domain
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
}
