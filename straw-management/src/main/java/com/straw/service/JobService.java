package com.straw.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.straw.common.Result;
import com.straw.dto.JobScheduleDTO;
import com.straw.entity.JobSchedule;

import java.util.Map;

public interface JobService {

    Result<?> create(JobScheduleDTO dto);

    Result<Page<JobSchedule>> list(Long orderId, int page, int size);

    Result<?> updateProgress(Long id, Integer progress);

    Result<Map<String, Object>> statistics();
}
