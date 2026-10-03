package com.example.mall.module.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * ClassName: Goods
 * Package: com.example.mall.module.entity
 * Description:
 *
 * @Author
 * @Create 2026/9/26 14:46
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("goods")
public class Goods {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String title;
    private String images;
    private Float price;
    private String description;
    private Integer createTime;
    private Integer updateTime;
    @TableLogic(value = "0", delval = "1")
    private Integer isDeleted;
}
