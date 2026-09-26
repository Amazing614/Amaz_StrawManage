package com.straw.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderExportDTO {

    @ExcelProperty("订单编号")
    private String orderNo;

    @ExcelProperty("秸秆信息ID")
    private Long strawInfoId;

    @ExcelProperty("用户ID")
    private Long userId;

    @ExcelProperty("作业主体ID")
    private Long jobEntityId;

    @ExcelProperty("费用(元)")
    private BigDecimal fee;

    @ExcelProperty("订单状态")
    private String statusText;

    @ExcelProperty("备注")
    private String remark;

    @ExcelProperty("创建时间")
    private String createTime;

    @ExcelProperty("接单时间")
    private String acceptTime;

    @ExcelProperty("完成时间")
    private String completeTime;

    @ExcelProperty("结算时间")
    private String settleTime;
}
