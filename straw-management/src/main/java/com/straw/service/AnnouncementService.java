package com.straw.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.straw.common.Result;
import com.straw.dto.AnnouncementDTO;
import com.straw.entity.Announcement;

public interface AnnouncementService {

    Result<?> create(AnnouncementDTO dto);

    Result<Page<Announcement>> list(int page, int size);

    Result<?> publishedList(int page, int size);

    Result<Announcement> getById(Long id);

    Result<?> update(Long id, AnnouncementDTO dto);

    Result<?> delete(Long id);
}
