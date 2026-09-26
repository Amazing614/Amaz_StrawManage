package com.straw.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StrawInfoExportDTO {

    @ExcelProperty("ID")
    private Long id;

    @ExcelProperty("秸秆类型")
    private String strawType;

    @ExcelProperty("产量(吨)")
    private BigDecimal quantity;

    @ExcelProperty("面积(亩)")
    private BigDecimal area;

    @ExcelProperty("位置")
    private String location;

    @ExcelProperty("描述")
    private String description;

    @ExcelProperty("审核状态")
    private String statusText;

    @ExcelProperty("审核备注")
    private String auditRemark;

    @ExcelProperty("上报时间")
    private String createTime;
}
