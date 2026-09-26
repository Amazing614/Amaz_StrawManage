package com.straw.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.straw.common.Result;
import com.straw.entity.Notification;
import com.straw.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/list")
    public Result<Page<Notification>> list(
            @RequestParam(required = false) Integer isRead,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Long userId = getCurrentUserId();
        return notificationService.list(userId, isRead, page, size);
    }

    @GetMapping("/unread-count")
    public Result<Integer> unreadCount() {
        Long userId = getCurrentUserId();
        return notificationService.unreadCount(userId);
    }

    @PutMapping("/{id}/read")
    public Result<?> markRead(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        return notificationService.markRead(id, userId);
    }

    @PutMapping("/read-all")
    public Result<?> markAllRead() {
        Long userId = getCurrentUserId();
        return notificationService.markAllRead(userId);
    }

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return Long.parseLong(auth.getName());
    }
}
