package com.straw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.straw.entity.Announcement;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AnnouncementMapper extends BaseMapper<Announcement> {
}
