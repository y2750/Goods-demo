package com.yy.app.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

/**
 * ClassName: GoodsListVO
 * Package: com.yy.app.domain
 * Description:
 *
 * @Author
 * @Create 2026/9/27 21:50
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
public class GoodsListVo {
    private BigInteger goodsId;
    private String title;
    private String mainImages;
    private Double price;
}
