package com.straw.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FaqDTO {

    @NotBlank(message = "问题不能为空")
    private String question;

    @NotBlank(message = "回答不能为空")
    private String answer;

    private String category;

    private Integer sortOrder;

    private Integer status;
}
