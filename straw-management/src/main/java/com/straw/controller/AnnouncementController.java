package com.straw.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.straw.common.Result;
import com.straw.dto.AnnouncementDTO;
import com.straw.entity.Announcement;
import com.straw.service.AnnouncementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/announcement")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> create(@Valid @RequestBody AnnouncementDTO dto) {
        return announcementService.create(dto);
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Page<Announcement>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return announcementService.list(page, size);
    }

    @GetMapping("/published")
    public Result<?> publishedList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return announcementService.publishedList(page, size);
    }

    @GetMapping("/{id}")
    public Result<Announcement> getById(@PathVariable Long id) {
        return announcementService.getById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> update(@PathVariable Long id, @Valid @RequestBody AnnouncementDTO dto) {
        return announcementService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> delete(@PathVariable Long id) {
        return announcementService.delete(id);
    }
}
