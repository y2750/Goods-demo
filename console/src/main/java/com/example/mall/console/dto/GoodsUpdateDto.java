package com.example.mall.console.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * ClassName: GoodsUpdateDto
 * Package: com.example.mall.console.dto
 * Description:
 *
 * @Author
 * @Create 2026/10/2 10:34
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class GoodsUpdateDto {
    private Integer goodsId;
    private String title;
    private String goodsImages;
    private Float price;
    private String description;
}
