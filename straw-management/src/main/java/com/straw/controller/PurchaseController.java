package com.straw.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.straw.common.Result;
import com.straw.dto.PurchaseDTO;
import com.straw.entity.Purchase;
import com.straw.service.PurchaseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @PostMapping("/create")
    public Result<?> create(@Valid @RequestBody PurchaseDTO dto, Authentication auth) {
        Long buyerId = Long.parseLong(auth.getName());
        return purchaseService.create(buyerId, dto);
    }

    @GetMapping("/list")
    public Result<Page<Purchase>> list(
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication auth) {
        Long userId = Long.parseLong(auth.getName());
        return purchaseService.list(userId, status, keyword, startDate, endDate, page, size);
    }

    @PutMapping("/{id}/confirm")
    public Result<?> confirm(@PathVariable Long id) {
        return purchaseService.confirm(id);
    }

    @PutMapping("/{id}/complete")
    public Result<?> complete(@PathVariable Long id) {
        return purchaseService.complete(id);
    }
}
