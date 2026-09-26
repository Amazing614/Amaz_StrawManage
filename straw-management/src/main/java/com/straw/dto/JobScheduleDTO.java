package com.straw.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class JobScheduleDTO {

    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @NotNull(message = "计划日期不能为空")
    private LocalDate planDate;

    @NotNull(message = "工人数量不能为空")
    private Integer workers;

    private String remark;
}
