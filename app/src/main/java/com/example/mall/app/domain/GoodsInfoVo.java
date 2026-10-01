package com.example.mall.app.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * ClassName: GoodsInfoVo
 * Package: com.example.mall.app.domain
 * Description:
 *
 * @Author
 * @Create 2026/9/27 20:58
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
public class GoodsInfoVo {
    private Integer goodsId;
    private String title;
    private List<String> goodsImages;
    private Float price;
    private String description;
}
