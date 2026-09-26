package com.straw.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class StrawInfoDTO {

    private Long regionId;

    @NotBlank(message = "秸秆类型不能为空")
    private String strawType;

    @NotNull(message = "产量不能为空")
    private BigDecimal quantity;

    private BigDecimal area;

    private String location;

    private String description;

    private String image;

    private Long plotId;
}
