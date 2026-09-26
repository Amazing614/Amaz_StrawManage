package com.straw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.straw.entity.JobSchedule;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JobScheduleMapper extends BaseMapper<JobSchedule> {
}
