package com.example.mall.app.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * ClassName: GoodsListVo
 * Package: com.example.mall.app.domain
 * Description:
 *
 * @Author
 * @Create 2026/9/27 21:50
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
public class GoodsListVo {
    private Integer goodsId;
    private String title;
    private String mainImage;
    private Float price;
}
