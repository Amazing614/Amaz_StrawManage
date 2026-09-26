package com.straw.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.straw.common.Result;
import com.straw.entity.Purchase;
import com.straw.entity.StrawInfo;
import com.straw.entity.User;
import com.straw.service.AdminService;
import com.straw.service.PurchaseService;
import com.straw.service.StrawInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private StrawInfoService strawInfoService;

    @Autowired
    private PurchaseService purchaseService;

    @GetMapping("/users")
    public Result<Page<User>> userList(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return adminService.userList(keyword, page, size);
    }

    @PutMapping("/user/{id}/status")
    public Result<?> updateUserStatus(@PathVariable Long id, @RequestParam Integer status) {
        return adminService.updateUserStatus(id, status);
    }

    @GetMapping("/dashboard")
    public Result<Map<String, Object>> dashboard() {
        return adminService.dashboard();
    }

    @GetMapping("/statistics/region")
    public Result<?> regionStatistics() {
        return adminService.regionStatistics();
    }

    @PutMapping("/straw/{id}/audit")
    public Result<?> auditStraw(@PathVariable Long id, @RequestParam Integer status,
                                @RequestParam(required = false) String remark) {
        return strawInfoService.audit(id, status, remark);
    }

    @GetMapping("/straw/list")
    public Result<Page<StrawInfo>> strawList(
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return strawInfoService.list(null, status, page, size);
    }

    @GetMapping("/purchase/list")
    public Result<Page<Purchase>> purchaseList(
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return purchaseService.list(null, status, null, null, null, page, size);
    }
}
