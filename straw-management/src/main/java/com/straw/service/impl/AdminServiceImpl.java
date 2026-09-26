package com.straw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.straw.common.Result;
import com.straw.entity.Order;
import com.straw.entity.Region;
import com.straw.entity.StrawInfo;
import com.straw.entity.User;
import com.straw.mapper.OrderMapper;
import com.straw.mapper.RegionMapper;
import com.straw.mapper.StrawInfoMapper;
import com.straw.mapper.UserMapper;
import com.straw.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StrawInfoMapper strawInfoMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RegionMapper regionMapper;

    @Override
    public Result<Page<User>> userList(String keyword, int page, int size) {
        Page<User> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();

        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(User::getUsername, keyword)
                    .or()
                    .like(User::getRealName, keyword)
                    .or()
                    .like(User::getPhone, keyword);
        }
        wrapper.orderByDesc(User::getCreateTime);

        Page<User> result = userMapper.selectPage(pageParam, wrapper);
        return Result.success(result);
    }

    @Override
    public Result<?> updateUserStatus(Long userId, Integer status) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }

        user.setStatus(status);
        userMapper.updateById(user);
        return Result.success("状态更新成功", null);
    }

    @Override
    public Result<Map<String, Object>> dashboard() {
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("userCount", userMapper.selectCount(null));
        dashboard.put("strawCount", strawInfoMapper.selectCount(null));
        dashboard.put("orderCount", orderMapper.selectCount(null));

        LambdaQueryWrapper<Order> completedWrapper = new LambdaQueryWrapper<>();
        completedWrapper.eq(Order::getStatus, 2);
        dashboard.put("completedOrderCount", orderMapper.selectCount(completedWrapper));

        // Order status distribution
        Map<Integer, Long> orderStatusMap = new HashMap<>();
        for (int i = 0; i <= 4; i++) {
            LambdaQueryWrapper<Order> w = new LambdaQueryWrapper<>();
            w.eq(Order::getStatus, i);
            orderStatusMap.put(i, orderMapper.selectCount(w));
        }
        dashboard.put("orderStatusMap", orderStatusMap);

        return Result.success(dashboard);
    }

    @Override
    public Result<?> regionStatistics() {
        List<StrawInfo> allStrawInfo = strawInfoMapper.selectList(null);
        Map<Long, java.math.BigDecimal> regionMap = new HashMap<>();

        for (StrawInfo info : allStrawInfo) {
            Long regionId = info.getRegionId();
            if (regionId != null) {
                regionMap.merge(regionId,
                        info.getQuantity() != null ? info.getQuantity() : java.math.BigDecimal.ZERO,
                        java.math.BigDecimal::add);
            }
        }

        List<Map<String, Object>> statistics = new ArrayList<>();
        for (Map.Entry<Long, java.math.BigDecimal> entry : regionMap.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("regionId", entry.getKey());

            Region region = regionMapper.selectById(entry.getKey());
            item.put("regionName", region != null ? region.getName() : "未知");
            item.put("quantity", entry.getValue());

            statistics.add(item);
        }

        return Result.success(statistics);
    }
}
