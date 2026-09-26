package com.straw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.straw.common.Result;
import com.straw.dto.PurchaseDTO;
import com.straw.entity.Purchase;
import com.straw.entity.StrawInfo;
import com.straw.entity.User;
import com.straw.mapper.PurchaseMapper;
import com.straw.mapper.StrawInfoMapper;
import com.straw.mapper.UserMapper;
import com.straw.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    private PurchaseMapper purchaseMapper;

    @Autowired
    private StrawInfoMapper strawInfoMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result<?> create(Long buyerId, PurchaseDTO dto) {
        StrawInfo strawInfo = strawInfoMapper.selectById(dto.getStrawInfoId());
        if (strawInfo == null) {
            return Result.error("秸秆信息不存在");
        }
        if (strawInfo.getStatus() != 1) {
            return Result.error("该秸秆信息未通过审核，无法收购");
        }
        if (dto.getQuantity().compareTo(strawInfo.getQuantity()) > 0) {
            return Result.error("收购量不能超过秸秆可用数量(" + strawInfo.getQuantity() + "吨)");
        }

        Purchase purchase = new Purchase();
        purchase.setStrawInfoId(dto.getStrawInfoId());
        purchase.setFarmerId(strawInfo.getUserId());
        purchase.setBuyerId(buyerId);
        purchase.setQuantity(dto.getQuantity());
        purchase.setPrice(dto.getPrice());
        purchase.setTotalAmount(dto.getQuantity().multiply(dto.getPrice()));
        purchase.setPurchaseTime(LocalDateTime.now());
        purchase.setStatus(0);
        purchase.setRemark(dto.getRemark());
        purchaseMapper.insert(purchase);
        return Result.success("收购登记成功", null);
    }

    @Override
    public Result<Page<Purchase>> list(Long userId, Integer status, String keyword,
                                       String startDate, String endDate, int page, int size) {
        Page<Purchase> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Purchase> wrapper = new LambdaQueryWrapper<>();

        if (userId != null) {
            wrapper.and(w -> w.eq(Purchase::getFarmerId, userId).or().eq(Purchase::getBuyerId, userId));
        }
        if (status != null) {
            wrapper.eq(Purchase::getStatus, status);
        }
        wrapper.orderByDesc(Purchase::getCreateTime);

        Page<Purchase> result = purchaseMapper.selectPage(pageParam, wrapper);
        List<Purchase> records = result.getRecords();

        // Populate user names
        Set<Long> userIds = new HashSet<>();
        records.forEach(p -> { userIds.add(p.getFarmerId()); userIds.add(p.getBuyerId()); });
        userIds.remove(null);
        if (!userIds.isEmpty()) {
            Map<Long, String> userNameMap = userMapper.selectBatchIds(userIds).stream()
                    .collect(Collectors.toMap(User::getId, u -> u.getRealName() != null ? u.getRealName() : u.getUsername()));
            records.forEach(p -> {
                p.setFarmerName(userNameMap.getOrDefault(p.getFarmerId(), ""));
                p.setBuyerName(userNameMap.getOrDefault(p.getBuyerId(), ""));
            });
        }

        // Populate straw info
        Set<Long> strawInfoIds = records.stream().map(Purchase::getStrawInfoId).filter(Objects::nonNull).collect(Collectors.toSet());
        if (!strawInfoIds.isEmpty()) {
            Map<Long, StrawInfo> strawMap = strawInfoMapper.selectBatchIds(strawInfoIds).stream()
                    .collect(Collectors.toMap(StrawInfo::getId, s -> s));
            records.forEach(p -> {
                StrawInfo si = strawMap.get(p.getStrawInfoId());
                if (si != null) {
                    p.setStrawType(si.getStrawType());
                    p.setStrawLocation(si.getLocation());
                }
            });
        }

        return Result.success(result);
    }

    @Override
    public Result<?> confirm(Long id) {
        Purchase purchase = purchaseMapper.selectById(id);
        if (purchase == null) {
            return Result.error("收购记录不存在");
        }
        purchase.setStatus(1);
        purchaseMapper.updateById(purchase);
        return Result.success("确认成功", null);
    }

    @Override
    public Result<?> complete(Long id) {
        Purchase purchase = purchaseMapper.selectById(id);
        if (purchase == null) {
            return Result.error("收购记录不存在");
        }
        purchase.setStatus(2);
        purchaseMapper.updateById(purchase);
        return Result.success("已完成", null);
    }
}
