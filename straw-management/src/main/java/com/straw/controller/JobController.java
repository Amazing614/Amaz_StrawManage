package com.straw.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.straw.common.Result;
import com.straw.dto.JobScheduleDTO;
import com.straw.entity.JobEntity;
import com.straw.entity.JobSchedule;
import com.straw.mapper.JobEntityMapper;
import com.straw.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/job")
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private JobEntityMapper jobEntityMapper;

    @PostMapping("/schedule")
    public Result<?> create(@RequestBody JobScheduleDTO dto) {
        return jobService.create(dto);
    }

    @GetMapping("/list")
    public Result<Page<JobSchedule>> list(
            @RequestParam(required = false) Long orderId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return jobService.list(orderId, page, size);
    }

    @PutMapping("/{id}/progress")
    public Result<?> updateProgress(@PathVariable Long id, @RequestParam Integer progress) {
        return jobService.updateProgress(id, progress);
    }

    @GetMapping("/statistics")
    public Result<Map<String, Object>> statistics() {
        return jobService.statistics();
    }

    @GetMapping("/providers")
    public Result<List<JobEntity>> providers() {
        LambdaQueryWrapper<JobEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(JobEntity::getStatus, 1);
        return Result.success(jobEntityMapper.selectList(wrapper));
    }
}
