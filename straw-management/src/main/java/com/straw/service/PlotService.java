package com.straw.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.straw.common.Result;
import com.straw.dto.PlotDTO;
import com.straw.entity.Plot;

public interface PlotService {

    Result<?> create(Long userId, PlotDTO dto);

    Result<Page<Plot>> list(Long userId, int page, int size);

    Result<?> update(Long id, Long userId, PlotDTO dto);

    Result<?> delete(Long id, Long userId);
}
