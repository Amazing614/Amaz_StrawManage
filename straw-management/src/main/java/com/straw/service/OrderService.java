package com.straw.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.straw.common.Result;
import com.straw.dto.OrderCreateDTO;
import com.straw.entity.Order;

public interface OrderService {

    Result<?> create(Long userId, OrderCreateDTO dto);

    Result<Page<Order>> list(Long userId, Integer role, Integer status, int page, int size);

    Result<Order> getById(Long id);

    Result<?> accept(Long id, Long jobEntityId);

    Result<?> complete(Long id);

    Result<?> settle(Long id);

    Result<?> cancel(Long id, Long userId);
}
