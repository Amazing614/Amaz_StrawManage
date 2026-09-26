package com.straw.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.straw.dto.OrderExportDTO;
import com.straw.dto.PurchaseExportDTO;
import com.straw.dto.StrawInfoExportDTO;
import com.straw.entity.Order;
import com.straw.entity.Purchase;
import com.straw.entity.StrawInfo;
import com.straw.entity.User;
import com.straw.mapper.OrderMapper;
import com.straw.mapper.PurchaseMapper;
import com.straw.mapper.StrawInfoMapper;
import com.straw.mapper.UserMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/export")
public class ExportController {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Map<Integer, String> STRAW_STATUS = Map.of(0, "待审核", 1, "已通过", 2, "已驳回");
    private static final Map<Integer, String> ORDER_STATUS = Map.of(0, "待接单", 1, "进行中", 2, "已完成", 3, "已结算", 4, "已取消");
    private static final Map<Integer, String> PURCHASE_STATUS = Map.of(0, "待确认", 1, "已确认", 2, "已完成");

    @Autowired
    private StrawInfoMapper strawInfoMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private PurchaseMapper purchaseMapper;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/straw")
    public void exportStraw(@RequestParam(required = false) Integer status,
                            HttpServletResponse response) throws Exception {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition",
                "attachment;filename=" + URLEncoder.encode("秸秆信息.xlsx", StandardCharsets.UTF_8));

        LambdaQueryWrapper<StrawInfo> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(StrawInfo::getStatus, status);
        }
        wrapper.orderByDesc(StrawInfo::getCreateTime);
        List<StrawInfo> list = strawInfoMapper.selectList(wrapper);

        List<StrawInfoExportDTO> data = new ArrayList<>();
        for (StrawInfo s : list) {
            StrawInfoExportDTO dto = new StrawInfoExportDTO();
            dto.setId(s.getId());
            dto.setStrawType(s.getStrawType());
            dto.setQuantity(s.getQuantity());
            dto.setArea(s.getArea());
            dto.setLocation(s.getLocation());
            dto.setDescription(s.getDescription());
            dto.setStatusText(STRAW_STATUS.getOrDefault(s.getStatus(), "未知"));
            dto.setAuditRemark(s.getAuditRemark());
            dto.setCreateTime(s.getCreateTime() != null ? s.getCreateTime().format(FMT) : "");
            data.add(dto);
        }

        EasyExcel.write(response.getOutputStream(), StrawInfoExportDTO.class)
                .sheet("秸秆信息")
                .doWrite(data);
    }

    @GetMapping("/order")
    public void exportOrder(@RequestParam(required = false) Integer status,
                            HttpServletResponse response) throws Exception {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition",
                "attachment;filename=" + URLEncoder.encode("订单列表.xlsx", StandardCharsets.UTF_8));

        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        wrapper.orderByDesc(Order::getCreateTime);
        List<Order> list = orderMapper.selectList(wrapper);

        List<OrderExportDTO> data = new ArrayList<>();
        for (Order o : list) {
            OrderExportDTO dto = new OrderExportDTO();
            dto.setOrderNo(o.getOrderNo());
            dto.setStrawInfoId(o.getStrawInfoId());
            dto.setUserId(o.getUserId());
            dto.setJobEntityId(o.getJobEntityId());
            dto.setFee(o.getFee());
            dto.setStatusText(ORDER_STATUS.getOrDefault(o.getStatus(), "未知"));
            dto.setRemark(o.getRemark());
            dto.setCreateTime(o.getCreateTime() != null ? o.getCreateTime().format(FMT) : "");
            dto.setAcceptTime(o.getAcceptTime() != null ? o.getAcceptTime().format(FMT) : "");
            dto.setCompleteTime(o.getCompleteTime() != null ? o.getCompleteTime().format(FMT) : "");
            dto.setSettleTime(o.getSettleTime() != null ? o.getSettleTime().format(FMT) : "");
            data.add(dto);
        }

        EasyExcel.write(response.getOutputStream(), OrderExportDTO.class)
                .sheet("订单列表")
                .doWrite(data);
    }

    @GetMapping("/purchase")
    public void exportPurchase(@RequestParam(required = false) Integer status,
                               HttpServletResponse response) throws Exception {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition",
                "attachment;filename=" + URLEncoder.encode("收购记录.xlsx", StandardCharsets.UTF_8));

        LambdaQueryWrapper<Purchase> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(Purchase::getStatus, status);
        }
        wrapper.orderByDesc(Purchase::getCreateTime);
        List<Purchase> list = purchaseMapper.selectList(wrapper);

        // Build lookup maps for names and straw info
        Set<Long> userIds = new HashSet<>();
        list.forEach(p -> { userIds.add(p.getFarmerId()); userIds.add(p.getBuyerId()); });
        userIds.remove(null);
        Map<Long, String> userNameMap = userIds.isEmpty() ? Map.of() :
                userMapper.selectBatchIds(userIds).stream()
                        .collect(Collectors.toMap(User::getId, u -> u.getRealName() != null ? u.getRealName() : u.getUsername()));

        Set<Long> strawInfoIds = list.stream().map(Purchase::getStrawInfoId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, StrawInfo> strawMap = strawInfoIds.isEmpty() ? Map.of() :
                strawInfoMapper.selectBatchIds(strawInfoIds).stream()
                        .collect(Collectors.toMap(StrawInfo::getId, s -> s));

        List<PurchaseExportDTO> data = new ArrayList<>();
        for (Purchase p : list) {
            PurchaseExportDTO dto = new PurchaseExportDTO();
            dto.setId(p.getId());
            dto.setFarmerName(userNameMap.getOrDefault(p.getFarmerId(), ""));
            dto.setBuyerName(userNameMap.getOrDefault(p.getBuyerId(), ""));
            StrawInfo si = strawMap.get(p.getStrawInfoId());
            if (si != null) {
                dto.setStrawType(si.getStrawType());
                dto.setStrawLocation(si.getLocation());
            }
            dto.setQuantity(p.getQuantity());
            dto.setPrice(p.getPrice());
            dto.setTotalAmount(p.getTotalAmount());
            dto.setStatusText(PURCHASE_STATUS.getOrDefault(p.getStatus(), "未知"));
            dto.setRemark(p.getRemark());
            dto.setCreateTime(p.getCreateTime() != null ? p.getCreateTime().format(FMT) : "");
            data.add(dto);
        }

        EasyExcel.write(response.getOutputStream(), PurchaseExportDTO.class)
                .sheet("收购记录")
                .doWrite(data);
    }
}
