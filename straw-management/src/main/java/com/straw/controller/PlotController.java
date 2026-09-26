package com.straw.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.straw.common.Result;
import com.straw.dto.PlotDTO;
import com.straw.entity.Plot;
import com.straw.service.PlotService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plot")
public class PlotController {

    @Autowired
    private PlotService plotService;

    @PostMapping("/create")
    public Result<?> create(@Valid @RequestBody PlotDTO dto, Authentication auth) {
        Long userId = Long.parseLong(auth.getName());
        return plotService.create(userId, dto);
    }

    @GetMapping("/list")
    public Result<Page<Plot>> list(
            Authentication auth,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Long userId = Long.parseLong(auth.getName());
        return plotService.list(userId, page, size);
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @Valid @RequestBody PlotDTO dto, Authentication auth) {
        Long userId = Long.parseLong(auth.getName());
        return plotService.update(id, userId, dto);
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id, Authentication auth) {
        Long userId = Long.parseLong(auth.getName());
        return plotService.delete(id, userId);
    }
}
