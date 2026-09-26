package com.straw.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("straw_info")
public class StrawInfo {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long regionId;

    private String strawType;

    private BigDecimal quantity;

    private BigDecimal area;

    private String location;

    private String description;

    private String image;

    private String auditRemark;

    private Long plotId;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private String userName;
}
