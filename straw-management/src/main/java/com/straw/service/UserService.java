package com.straw.service;

import com.straw.common.Result;
import com.straw.dto.*;
import com.straw.entity.User;

public interface UserService {

    Result<?> register(RegisterDTO dto);

    Result<String> login(LoginDTO dto);

    Result<User> getUserInfo(Long userId);

    Result<?> updateUserInfo(Long userId, UserUpdateDTO dto);

    Result<?> changePassword(Long userId, PasswordDTO dto);
}
