package com.straw.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.straw.common.Result;
import com.straw.dto.OrderCreateDTO;
import com.straw.entity.Order;
import com.straw.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public Result<?> create(@RequestBody OrderCreateDTO dto) {
        Long userId = getCurrentUserId();
        return orderService.create(userId, dto);
    }

    @GetMapping("/list")
    public Result<Page<Order>> list(
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Long userId = getCurrentUserId();
        Integer role = getCurrentUserRole();
        return orderService.list(userId, role, status, page, size);
    }

    @GetMapping("/{id}")
    public Result<Order> getById(@PathVariable Long id) {
        return orderService.getById(id);
    }

    @PutMapping("/{id}/accept")
    public Result<?> accept(@PathVariable Long id, @RequestParam(required = false) Long jobEntityId) {
        Integer role = getCurrentUserRole();
        if (role != 2 && role != 3 && role != 4) {
            return Result.error("无权执行接单操作");
        }
        if (jobEntityId == null) {
            jobEntityId = getCurrentUserId();
        }
        return orderService.accept(id, jobEntityId);
    }

    @PutMapping("/{id}/complete")
    public Result<?> complete(@PathVariable Long id) {
        Integer role = getCurrentUserRole();
        if (role != 2 && role != 3 && role != 4) {
            return Result.error("无权执行完成操作");
        }
        return orderService.complete(id);
    }

    @PutMapping("/{id}/settle")
    public Result<?> settle(@PathVariable Long id) {
        Integer role = getCurrentUserRole();
        if (role != 3 && role != 4) {
            return Result.error("无权执行结算操作");
        }
        return orderService.settle(id);
    }

    @PutMapping("/{id}/cancel")
    public Result<?> cancel(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        return orderService.cancel(id, userId);
    }

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return Long.parseLong(auth.getName());
    }

    private Integer getCurrentUserRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authority = auth.getAuthorities().iterator().next().getAuthority();
        return switch (authority) {
            case "ROLE_FARMER" -> 0;
            case "ROLE_COOPERATIVE" -> 1;
            case "ROLE_WORKER" -> 2;
            case "ROLE_ADMIN" -> 3;
            case "ROLE_ENTERPRISE" -> 4;
            default -> -1;
        };
    }
}
