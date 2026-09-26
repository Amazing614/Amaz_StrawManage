package com.straw.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class OrderCreateDTO {

    @NotNull(message = "秸秆信息ID不能为空")
    private Long strawInfoId;

    private Long jobEntityId;

    private BigDecimal fee;

    private String remark;
}
