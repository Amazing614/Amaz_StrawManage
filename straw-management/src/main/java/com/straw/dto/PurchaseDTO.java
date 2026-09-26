package com.straw.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseDTO {
    @NotNull(message = "秸秆信息ID不能为空")
    private Long strawInfoId;

    @NotNull(message = "收购量不能为空")
    private BigDecimal quantity;

    @NotNull(message = "收购单价不能为空")
    private BigDecimal price;

    private String remark;
}
