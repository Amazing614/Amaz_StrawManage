package com.straw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.straw.common.Result;
import com.straw.dto.JobScheduleDTO;
import com.straw.entity.JobEntity;
import com.straw.entity.JobSchedule;
import com.straw.entity.Order;
import com.straw.entity.User;
import com.straw.mapper.JobEntityMapper;
import com.straw.mapper.JobScheduleMapper;
import com.straw.mapper.OrderMapper;
import com.straw.mapper.UserMapper;
import com.straw.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobScheduleMapper jobScheduleMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JobEntityMapper jobEntityMapper;

    @Override
    public Result<?> create(JobScheduleDTO dto) {
        JobSchedule schedule = new JobSchedule();
        schedule.setOrderId(dto.getOrderId());
        schedule.setPlanDate(dto.getPlanDate());
        schedule.setWorkers(dto.getWorkers());
        schedule.setProgress(0);
        schedule.setStatus(0);
        schedule.setRemark(dto.getRemark());

        jobScheduleMapper.insert(schedule);
        return Result.success("排程创建成功", null);
    }

    @Override
    public Result<Page<JobSchedule>> list(Long orderId, int page, int size) {
        Page<JobSchedule> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<JobSchedule> wrapper = new LambdaQueryWrapper<>();

        if (orderId != null) {
            wrapper.eq(JobSchedule::getOrderId, orderId);
        }
        wrapper.orderByAsc(JobSchedule::getPlanDate);

        Page<JobSchedule> result = jobScheduleMapper.selectPage(pageParam, wrapper);
        List<JobSchedule> records = result.getRecords();
        if (!records.isEmpty()) {
            // Fetch related Orders
            Set<Long> orderIds = records.stream().map(JobSchedule::getOrderId).filter(Objects::nonNull).collect(Collectors.toSet());
            if (!orderIds.isEmpty()) {
                Map<Long, Order> orderMap = orderMapper.selectBatchIds(orderIds).stream()
                        .collect(Collectors.toMap(Order::getId, o -> o));

                // Populate orderNo
                records.forEach(s -> {
                    Order o = orderMap.get(s.getOrderId());
                    if (o != null) s.setOrderNo(o.getOrderNo());
                });

                // Populate demandUserName
                Set<Long> userIds = orderMap.values().stream().map(Order::getUserId).filter(Objects::nonNull).collect(Collectors.toSet());
                if (!userIds.isEmpty()) {
                    Map<Long, String> userNameMap = userMapper.selectBatchIds(userIds).stream()
                            .collect(Collectors.toMap(User::getId, u -> u.getRealName() != null ? u.getRealName() : u.getUsername()));
                    records.forEach(s -> {
                        Order o = orderMap.get(s.getOrderId());
                        if (o != null) s.setDemandUserName(userNameMap.getOrDefault(o.getUserId(), ""));
                    });
                }

                // Populate jobUserName
                Set<Long> jobEntityIds = orderMap.values().stream().map(Order::getJobEntityId).filter(Objects::nonNull).collect(Collectors.toSet());
                if (!jobEntityIds.isEmpty()) {
                    Map<Long, String> jobNameMap = jobEntityMapper.selectBatchIds(jobEntityIds).stream()
                            .collect(Collectors.toMap(JobEntity::getId, JobEntity::getCompanyName));
                    records.forEach(s -> {
                        Order o = orderMap.get(s.getOrderId());
                        if (o != null) s.setJobUserName(jobNameMap.getOrDefault(o.getJobEntityId(), ""));
                    });
                }
            }
        }
        return Result.success(result);
    }

    @Override
    public Result<?> updateProgress(Long id, Integer progress) {
        JobSchedule schedule = jobScheduleMapper.selectById(id);
        if (schedule == null) {
            return Result.error("排程不存在");
        }

        schedule.setProgress(progress);

        if (progress >= 100) {
            schedule.setStatus(2);
        } else if (progress > 0) {
            schedule.setStatus(1);
        }

        jobScheduleMapper.updateById(schedule);
        return Result.success("进度更新成功", null);
    }

    @Override
    public Result<Map<String, Object>> statistics() {
        Map<String, Object> stats = new HashMap<>();

        LambdaQueryWrapper<JobSchedule> wrapper0 = new LambdaQueryWrapper<>();
        wrapper0.eq(JobSchedule::getStatus, 0);
        stats.put("pending", jobScheduleMapper.selectCount(wrapper0));

        LambdaQueryWrapper<JobSchedule> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(JobSchedule::getStatus, 1);
        stats.put("inProgress", jobScheduleMapper.selectCount(wrapper1));

        LambdaQueryWrapper<JobSchedule> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(JobSchedule::getStatus, 2);
        stats.put("completed", jobScheduleMapper.selectCount(wrapper2));

        stats.put("total", jobScheduleMapper.selectCount(null));

        return Result.success(stats);
    }
}
