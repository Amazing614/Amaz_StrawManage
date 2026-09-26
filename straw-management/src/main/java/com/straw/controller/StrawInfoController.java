package com.straw.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.straw.common.Result;
import com.straw.dto.StrawInfoDTO;
import com.straw.entity.StrawInfo;
import com.straw.service.StrawInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/straw")
public class StrawInfoController {

    @Autowired
    private StrawInfoService strawInfoService;

    @PostMapping("/report")
    public Result<?> report(@RequestBody StrawInfoDTO dto) {
        Long userId = getCurrentUserId();
        return strawInfoService.report(userId, dto);
    }

    @GetMapping("/list")
    public Result<Page<StrawInfo>> list(
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Long userId = getCurrentUserId();
        return strawInfoService.list(userId, status, page, size);
    }

    @GetMapping("/{id}")
    public Result<StrawInfo> getById(@PathVariable Long id) {
        return strawInfoService.getById(id);
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody StrawInfoDTO dto) {
        Long userId = getCurrentUserId();
        return strawInfoService.update(id, userId, dto);
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        return strawInfoService.delete(id, userId);
    }

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return Long.parseLong(auth.getName());
    }
}
