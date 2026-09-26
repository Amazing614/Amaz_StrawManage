package com.straw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.straw.common.Result;
import com.straw.entity.Notification;
import com.straw.mapper.NotificationMapper;
import com.straw.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Override
    public void create(Long userId, Integer type, String title, String content, Long relatedId) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType(type);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setRelatedId(relatedId);
        notification.setIsRead(0);
        notificationMapper.insert(notification);
    }

    @Override
    public Result<Page<Notification>> list(Long userId, Integer isRead, int page, int size) {
        Page<Notification> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getUserId, userId);
        if (isRead != null) {
            wrapper.eq(Notification::getIsRead, isRead);
        }
        wrapper.orderByDesc(Notification::getCreateTime);
        Page<Notification> result = notificationMapper.selectPage(pageParam, wrapper);
        return Result.success(result);
    }

    @Override
    public Result<Integer> unreadCount(Long userId) {
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getUserId, userId);
        wrapper.eq(Notification::getIsRead, 0);
        Long count = notificationMapper.selectCount(wrapper);
        return Result.success(count.intValue());
    }

    @Override
    public Result<?> markRead(Long id, Long userId) {
        Notification notification = notificationMapper.selectById(id);
        if (notification == null || !notification.getUserId().equals(userId)) {
            return Result.error("通知不存在");
        }
        notification.setIsRead(1);
        notificationMapper.updateById(notification);
        return Result.success("已读", null);
    }

    @Override
    public Result<?> markAllRead(Long userId) {
        LambdaUpdateWrapper<Notification> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Notification::getUserId, userId);
        wrapper.eq(Notification::getIsRead, 0);
        wrapper.set(Notification::getIsRead, 1);
        notificationMapper.update(null, wrapper);
        return Result.success("全部已读", null);
    }
}
