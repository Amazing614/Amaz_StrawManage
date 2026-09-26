package com.straw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.straw.common.Result;
import com.straw.dto.AnnouncementDTO;
import com.straw.entity.Announcement;
import com.straw.mapper.AnnouncementMapper;
import com.straw.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    @Autowired
    private AnnouncementMapper announcementMapper;

    @Override
    public Result<?> create(AnnouncementDTO dto) {
        Announcement announcement = new Announcement();
        announcement.setTitle(dto.getTitle());
        announcement.setContent(dto.getContent());
        announcement.setAuthor("admin");
        announcement.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        announcementMapper.insert(announcement);
        return Result.success("创建成功", null);
    }

    @Override
    public Result<Page<Announcement>> list(int page, int size) {
        Page<Announcement> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Announcement> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Announcement::getCreateTime);
        Page<Announcement> result = announcementMapper.selectPage(pageParam, wrapper);
        return Result.success(result);
    }

    @Override
    public Result<?> publishedList(int page, int size) {
        Page<Announcement> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Announcement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Announcement::getStatus, 1);
        wrapper.orderByDesc(Announcement::getCreateTime);
        Page<Announcement> result = announcementMapper.selectPage(pageParam, wrapper);
        return Result.success(result);
    }

    @Override
    public Result<Announcement> getById(Long id) {
        Announcement announcement = announcementMapper.selectById(id);
        if (announcement == null) {
            return Result.error("公告不存在");
        }
        return Result.success(announcement);
    }

    @Override
    public Result<?> update(Long id, AnnouncementDTO dto) {
        Announcement announcement = announcementMapper.selectById(id);
        if (announcement == null) {
            return Result.error("公告不存在");
        }
        announcement.setTitle(dto.getTitle());
        announcement.setContent(dto.getContent());
        if (dto.getStatus() != null) {
            announcement.setStatus(dto.getStatus());
        }
        announcementMapper.updateById(announcement);
        return Result.success("更新成功", null);
    }

    @Override
    public Result<?> delete(Long id) {
        Announcement announcement = announcementMapper.selectById(id);
        if (announcement == null) {
            return Result.error("公告不存在");
        }
        announcementMapper.deleteById(id);
        return Result.success("删除成功", null);
    }
}
