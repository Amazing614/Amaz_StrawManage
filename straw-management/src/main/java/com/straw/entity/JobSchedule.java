package com.straw.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("job_schedule")
public class JobSchedule {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long orderId;

    private LocalDate planDate;

    private Integer workers;

    private Integer progress;

    private Integer status;

    private String remark;

    @TableField(exist = false)
    private String orderNo;

    @TableField(exist = false)
    private String demandUserName;

    @TableField(exist = false)
    private String jobUserName;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
