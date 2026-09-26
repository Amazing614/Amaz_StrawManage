package com.straw.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.straw.common.Result;
import com.straw.entity.Notification;

public interface NotificationService {

    void create(Long userId, Integer type, String title, String content, Long relatedId);

    Result<Page<Notification>> list(Long userId, Integer isRead, int page, int size);

    Result<Integer> unreadCount(Long userId);

    Result<?> markRead(Long id, Long userId);

    Result<?> markAllRead(Long userId);
}
