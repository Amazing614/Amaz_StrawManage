package com.straw.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("purchase")
public class Purchase {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long strawInfoId;

    private Long farmerId;

    private Long buyerId;

    private BigDecimal quantity;

    private BigDecimal price;

    private BigDecimal totalAmount;

    private LocalDateTime purchaseTime;

    private Integer status;

    private String remark;

    @TableField(exist = false)
    private String farmerName;

    @TableField(exist = false)
    private String buyerName;

    @TableField(exist = false)
    private String strawType;

    @TableField(exist = false)
    private String strawLocation;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
