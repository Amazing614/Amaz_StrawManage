package com.straw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.straw.common.JwtUtil;
import com.straw.common.Result;
import com.straw.dto.*;
import com.straw.entity.User;
import com.straw.mapper.UserMapper;
import com.straw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Result<?> register(RegisterDTO dto) {
        // Check username uniqueness
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getUsername());
        User existUser = userMapper.selectOne(wrapper);
        if (existUser != null) {
            return Result.error("用户名已存在");
        }

        // Validate role: only allow Farmer(0), Cooperative(1), Worker(2) for self-registration
        if (dto.getRole() == null || dto.getRole() < 0 || dto.getRole() > 2) {
            return Result.error("无效的角色选择");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPhone(dto.getPhone());
        user.setRealName(dto.getRealName());
        user.setRole(dto.getRole());
        user.setStatus(1);
        user.setVerified(0);

        userMapper.insert(user);
        return Result.success("注册成功", null);
    }

    @Override
    public Result<String> login(LoginDTO dto) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getUsername());
        User user = userMapper.selectOne(wrapper);

        if (user == null) {
            return Result.error("用户名或密码错误");
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return Result.error("用户名或密码错误");
        }

        if (user.getStatus() == 0) {
            return Result.error("账号已被禁用");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        return Result.success("登录成功", token);
    }

    @Override
    public Result<User> getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        return Result.success(user);
    }

    @Override
    public Result<?> updateUserInfo(Long userId, UserUpdateDTO dto) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }

        user.setPhone(dto.getPhone());
        user.setRealName(dto.getRealName());
        user.setAvatar(dto.getAvatar());

        userMapper.updateById(user);
        return Result.success("更新成功", null);
    }

    @Override
    public Result<?> changePassword(Long userId, PasswordDTO dto) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            return Result.error("原密码错误");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userMapper.updateById(user);
        return Result.success("密码修改成功", null);
    }
}
