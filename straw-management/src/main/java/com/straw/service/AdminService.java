package com.straw.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.straw.common.Result;
import com.straw.entity.User;

import java.util.Map;

public interface AdminService {

    Result<Page<User>> userList(String keyword, int page, int size);

    Result<?> updateUserStatus(Long userId, Integer status);

    Result<Map<String, Object>> dashboard();

    Result<?> regionStatistics();
}
