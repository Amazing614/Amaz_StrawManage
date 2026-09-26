package com.straw.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("`order`")
public class Order {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String orderNo;

    private Long strawInfoId;

    private Long userId;

    private Long jobEntityId;

    private Integer status;

    private BigDecimal fee;

    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField("accept_time")
    private LocalDateTime acceptTime;

    @TableField("complete_time")
    private LocalDateTime completeTime;

    @TableField("settle_time")
    private LocalDateTime settleTime;

    @TableField(exist = false)
    private String demandUserName;

    @TableField(exist = false)
    private String jobUserName;

    @TableField(exist = false)
    private String strawType;

    @TableField(exist = false)
    private BigDecimal strawQuantity;

    @TableField(exist = false)
    private BigDecimal strawArea;

    @TableField(exist = false)
    private String strawLocation;
}
