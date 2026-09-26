package com.straw.controller;

import com.straw.common.Result;
import com.straw.dto.*;
import com.straw.entity.User;
import com.straw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result<?> register(@RequestBody RegisterDTO dto) {
        return userService.register(dto);
    }

    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginDTO dto) {
        return userService.login(dto);
    }

    @GetMapping("/info")
    public Result<User> getUserInfo() {
        Long userId = getCurrentUserId();
        return userService.getUserInfo(userId);
    }

    @PutMapping("/info")
    public Result<?> updateUserInfo(@RequestBody UserUpdateDTO dto) {
        Long userId = getCurrentUserId();
        return userService.updateUserInfo(userId, dto);
    }

    @PutMapping("/password")
    public Result<?> changePassword(@RequestBody PasswordDTO dto) {
        Long userId = getCurrentUserId();
        return userService.changePassword(userId, dto);
    }

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return Long.parseLong(auth.getName());
    }
}
