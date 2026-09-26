package com.straw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.straw.common.Result;
import com.straw.dto.StrawInfoDTO;
import com.straw.entity.StrawInfo;
import com.straw.entity.User;
import com.straw.mapper.StrawInfoMapper;
import com.straw.mapper.UserMapper;
import com.straw.service.NotificationService;
import com.straw.service.StrawInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StrawInfoServiceImpl implements StrawInfoService {

    @Autowired
    private StrawInfoMapper strawInfoMapper;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result<?> report(Long userId, StrawInfoDTO dto) {
        StrawInfo strawInfo = new StrawInfo();
        strawInfo.setUserId(userId);
        strawInfo.setRegionId(dto.getRegionId());
        strawInfo.setStrawType(dto.getStrawType());
        strawInfo.setQuantity(dto.getQuantity());
        strawInfo.setArea(dto.getArea());
        strawInfo.setLocation(dto.getLocation());
        strawInfo.setDescription(dto.getDescription());
        strawInfo.setImage(dto.getImage());
        strawInfo.setPlotId(dto.getPlotId());
        strawInfo.setStatus(0);

        strawInfoMapper.insert(strawInfo);
        return Result.success("上报成功", null);
    }

    @Override
    public Result<Page<StrawInfo>> list(Long userId, Integer status, int page, int size) {
        Page<StrawInfo> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<StrawInfo> wrapper = new LambdaQueryWrapper<>();

        if (userId != null) {
            wrapper.eq(StrawInfo::getUserId, userId);
        }
        if (status != null) {
            wrapper.eq(StrawInfo::getStatus, status);
        }
        wrapper.orderByDesc(StrawInfo::getCreateTime);

        Page<StrawInfo> result = strawInfoMapper.selectPage(pageParam, wrapper);

        List<StrawInfo> records = result.getRecords();
        if (!records.isEmpty()) {
            Set<Long> userIds = records.stream().map(StrawInfo::getUserId).collect(Collectors.toSet());
            List<User> users = userMapper.selectBatchIds(userIds);
            Map<Long, String> userNameMap = users.stream()
                    .collect(Collectors.toMap(User::getId, u -> u.getRealName() != null ? u.getRealName() : u.getUsername()));
            records.forEach(s -> s.setUserName(userNameMap.getOrDefault(s.getUserId(), "")));
        }

        return Result.success(result);
    }

    @Override
    public Result<StrawInfo> getById(Long id) {
        StrawInfo strawInfo = strawInfoMapper.selectById(id);
        if (strawInfo == null) {
            return Result.error("秸秆信息不存在");
        }
        return Result.success(strawInfo);
    }

    @Override
    public Result<?> update(Long id, Long userId, StrawInfoDTO dto) {
        StrawInfo strawInfo = strawInfoMapper.selectById(id);
        if (strawInfo == null) {
            return Result.error("秸秆信息不存在");
        }
        if (!strawInfo.getUserId().equals(userId)) {
            return Result.error("无权修改他人信息");
        }

        strawInfo.setRegionId(dto.getRegionId());
        strawInfo.setStrawType(dto.getStrawType());
        strawInfo.setQuantity(dto.getQuantity());
        strawInfo.setArea(dto.getArea());
        strawInfo.setLocation(dto.getLocation());
        strawInfo.setDescription(dto.getDescription());
        strawInfo.setImage(dto.getImage());
        strawInfo.setPlotId(dto.getPlotId());

        strawInfoMapper.updateById(strawInfo);
        return Result.success("更新成功", null);
    }

    @Override
    public Result<?> delete(Long id, Long userId) {
        StrawInfo strawInfo = strawInfoMapper.selectById(id);
        if (strawInfo == null) {
            return Result.error("秸秆信息不存在");
        }
        if (!strawInfo.getUserId().equals(userId)) {
            return Result.error("无权删除他人信息");
        }

        strawInfoMapper.deleteById(id);
        return Result.success("删除成功", null);
    }

    @Override
    public Result<?> audit(Long id, Integer status, String remark) {
        StrawInfo strawInfo = strawInfoMapper.selectById(id);
        if (strawInfo == null) {
            return Result.error("秸秆信息不存在");
        }

        strawInfo.setStatus(status);
        strawInfo.setAuditRemark(remark);
        strawInfoMapper.updateById(strawInfo);

        String statusText = status == 1 ? "通过" : "驳回";
        String content = "您上报的秸秆信息(ID:" + id + ")已被" + statusText;
        if (remark != null && !remark.isEmpty()) {
            content += "，原因：" + remark;
        }
        notificationService.create(strawInfo.getUserId(), 1, "秸秆信息审核结果", content, id);

        return Result.success("审核成功", null);
    }
}
