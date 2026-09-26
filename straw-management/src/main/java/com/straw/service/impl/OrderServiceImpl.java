package com.straw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.straw.common.Result;
import com.straw.dto.OrderCreateDTO;
import com.straw.entity.JobEntity;
import com.straw.entity.Order;
import com.straw.entity.StrawInfo;
import com.straw.entity.User;
import com.straw.mapper.JobEntityMapper;
import com.straw.mapper.OrderMapper;
import com.straw.mapper.StrawInfoMapper;
import com.straw.mapper.UserMapper;
import com.straw.service.NotificationService;
import com.straw.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StrawInfoMapper strawInfoMapper;

    @Autowired
    private JobEntityMapper jobEntityMapper;

    @Override
    public Result<?> create(Long userId, OrderCreateDTO dto) {
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setStrawInfoId(dto.getStrawInfoId());
        order.setUserId(userId);
        order.setJobEntityId(dto.getJobEntityId());
        order.setStatus(0);
        order.setFee(dto.getFee());
        order.setRemark(dto.getRemark());

        orderMapper.insert(order);
        return Result.success("订单创建成功", null);
    }

    @Override
    public Result<Page<Order>> list(Long userId, Integer role, Integer status, int page, int size) {
        Page<Order> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();

        if (role != null && (role == 3 || role == 4)) {
            // Admin sees all orders, no user filter
        } else if (role != null && role == 2) {
            // Workers see all pending orders (to accept) + their own assigned orders
            wrapper.and(w -> w.isNull(Order::getJobEntityId)
                    .or()
                    .eq(Order::getJobEntityId, userId));
        } else if (userId != null) {
            wrapper.eq(Order::getUserId, userId);
        }

        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        wrapper.orderByDesc(Order::getCreateTime);

        Page<Order> result = orderMapper.selectPage(pageParam, wrapper);

        List<Order> records = result.getRecords();
        if (!records.isEmpty()) {
            // Populate demandUserName
            Set<Long> userIds = records.stream().map(Order::getUserId).filter(Objects::nonNull).collect(Collectors.toSet());
            if (!userIds.isEmpty()) {
                Map<Long, String> userNameMap = userMapper.selectBatchIds(userIds).stream()
                        .collect(Collectors.toMap(User::getId, u -> u.getRealName() != null ? u.getRealName() : u.getUsername()));
                records.forEach(o -> o.setDemandUserName(userNameMap.getOrDefault(o.getUserId(), "")));
            }

            // Populate jobUserName
            Set<Long> jobEntityIds = records.stream().map(Order::getJobEntityId).filter(Objects::nonNull).collect(Collectors.toSet());
            if (!jobEntityIds.isEmpty()) {
                Map<Long, String> jobNameMap = jobEntityMapper.selectBatchIds(jobEntityIds).stream()
                        .collect(Collectors.toMap(JobEntity::getId, JobEntity::getCompanyName));
                records.forEach(o -> {
                    if (o.getJobEntityId() != null) o.setJobUserName(jobNameMap.getOrDefault(o.getJobEntityId(), ""));
                });
            }

            // Populate straw info
            Set<Long> strawInfoIds = records.stream().map(Order::getStrawInfoId).filter(Objects::nonNull).collect(Collectors.toSet());
            if (!strawInfoIds.isEmpty()) {
                Map<Long, StrawInfo> strawMap = strawInfoMapper.selectBatchIds(strawInfoIds).stream()
                        .collect(Collectors.toMap(StrawInfo::getId, s -> s));
                records.forEach(o -> {
                    StrawInfo si = strawMap.get(o.getStrawInfoId());
                    if (si != null) {
                        o.setStrawType(si.getStrawType());
                        o.setStrawQuantity(si.getQuantity());
                        o.setStrawArea(si.getArea());
                        o.setStrawLocation(si.getLocation());
                    }
                });
            }
        }

        return Result.success(result);
    }

    @Override
    public Result<Order> getById(Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            return Result.error("订单不存在");
        }
        return Result.success(order);
    }

    @Override
    public Result<?> accept(Long id, Long jobEntityId) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            return Result.error("订单不存在");
        }
        if (order.getStatus() != 0) {
            return Result.error("订单状态不允许接单");
        }

        order.setStatus(1);
        order.setJobEntityId(jobEntityId);
        order.setAcceptTime(LocalDateTime.now());

        orderMapper.updateById(order);

        notificationService.create(order.getUserId(), 2, "订单已被接单",
                "您的订单(编号:" + order.getOrderNo() + ")已被接单，即将开始作业。", id);

        return Result.success("接单成功", null);
    }

    @Override
    public Result<?> complete(Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            return Result.error("订单不存在");
        }
        if (order.getStatus() != 1) {
            return Result.error("订单状态不允许完成");
        }

        order.setStatus(2);
        order.setCompleteTime(LocalDateTime.now());

        orderMapper.updateById(order);

        notificationService.create(order.getUserId(), 2, "订单已完成",
                "您的订单(编号:" + order.getOrderNo() + ")作业已完成。", id);

        return Result.success("订单已完成", null);
    }

    @Override
    public Result<?> settle(Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            return Result.error("订单不存在");
        }
        if (order.getStatus() != 2) {
            return Result.error("订单状态不允许结算");
        }

        order.setStatus(3);
        order.setSettleTime(LocalDateTime.now());

        orderMapper.updateById(order);

        notificationService.create(order.getUserId(), 2, "订单已结算",
                "您的订单(编号:" + order.getOrderNo() + ")已完成结算。", id);

        return Result.success("结算成功", null);
    }

    @Override
    public Result<?> cancel(Long id, Long userId) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            return Result.error("订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            return Result.error("无权取消此订单");
        }
        if (order.getStatus() != 0) {
            return Result.error("订单状态不允许取消");
        }

        order.setStatus(4);
        orderMapper.updateById(order);
        return Result.success("订单已取消", null);
    }

    private String generateOrderNo() {
        long timestamp = System.currentTimeMillis();
        int random = new Random().nextInt(9000) + 1000;
        return timestamp + "" + random;
    }
}
