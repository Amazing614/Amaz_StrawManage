package com.straw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.straw.common.Result;
import com.straw.dto.PlotDTO;
import com.straw.entity.Plot;
import com.straw.mapper.PlotMapper;
import com.straw.service.PlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlotServiceImpl implements PlotService {

    @Autowired
    private PlotMapper plotMapper;

    @Override
    public Result<?> create(Long userId, PlotDTO dto) {
        Plot plot = new Plot();
        plot.setUserId(userId);
        plot.setName(dto.getName());
        plot.setArea(dto.getArea());
        plot.setLocation(dto.getLocation());
        plot.setCropType(dto.getCropType());
        plot.setDescription(dto.getDescription());
        plotMapper.insert(plot);
        return Result.success("创建成功", null);
    }

    @Override
    public Result<Page<Plot>> list(Long userId, int page, int size) {
        Page<Plot> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Plot> wrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            wrapper.eq(Plot::getUserId, userId);
        }
        wrapper.orderByDesc(Plot::getCreateTime);
        Page<Plot> result = plotMapper.selectPage(pageParam, wrapper);
        return Result.success(result);
    }

    @Override
    public Result<?> update(Long id, Long userId, PlotDTO dto) {
        Plot plot = plotMapper.selectById(id);
        if (plot == null) {
            return Result.error("地块不存在");
        }
        if (!plot.getUserId().equals(userId)) {
            return Result.error("无权修改他人地块");
        }
        plot.setName(dto.getName());
        plot.setArea(dto.getArea());
        plot.setLocation(dto.getLocation());
        plot.setCropType(dto.getCropType());
        plot.setDescription(dto.getDescription());
        plotMapper.updateById(plot);
        return Result.success("更新成功", null);
    }

    @Override
    public Result<?> delete(Long id, Long userId) {
        Plot plot = plotMapper.selectById(id);
        if (plot == null) {
            return Result.error("地块不存在");
        }
        if (!plot.getUserId().equals(userId)) {
            return Result.error("无权删除他人地块");
        }
        plotMapper.deleteById(id);
        return Result.success("删除成功", null);
    }
}
