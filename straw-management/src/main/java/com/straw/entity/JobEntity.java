package com.straw.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("job_entity")
public class JobEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String companyName;

    private String qualification;

    private String serviceArea;

    private String equipment;

    private String contact;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
