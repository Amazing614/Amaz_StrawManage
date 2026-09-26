package com.straw.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseExportDTO {

    @ExcelProperty("ID")
    private Long id;

    @ExcelProperty("秸秆类型")
    private String strawType;

    @ExcelProperty("秸秆地址")
    private String strawLocation;

    @ExcelProperty("农户")
    private String farmerName;

    @ExcelProperty("收购方")
    private String buyerName;

    @ExcelProperty("数量(吨)")
    private BigDecimal quantity;

    @ExcelProperty("单价(元/吨)")
    private BigDecimal price;

    @ExcelProperty("总金额(元)")
    private BigDecimal totalAmount;

    @ExcelProperty("状态")
    private String statusText;

    @ExcelProperty("备注")
    private String remark;

    @ExcelProperty("创建时间")
    private String createTime;
}
