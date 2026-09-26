package com.straw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PlotDTO {
    @NotBlank(message = "地块名称不能为空")
    private String name;

    @NotNull(message = "面积不能为空")
    private BigDecimal area;

    @NotBlank(message = "地块位置不能为空")
    private String location;

    private String cropType;

    private String description;
}
