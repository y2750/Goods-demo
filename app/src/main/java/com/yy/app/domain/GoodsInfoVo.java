package com.yy.app.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;
import java.util.List;

/**
 * ClassName: GoodsVO
 * Package: com.yy.app.domain
 * Description:
 *
 * @Author
 * @Create 2026/9/27 20:58
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
public class GoodsInfoVo {
    private BigInteger goodsId;
    private String title;
    private List<String> goodsImages;
    private Double price;
    private String description;
}
