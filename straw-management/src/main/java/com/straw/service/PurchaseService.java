package com.straw.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.straw.common.Result;
import com.straw.dto.PurchaseDTO;
import com.straw.entity.Purchase;

public interface PurchaseService {

    Result<?> create(Long buyerId, PurchaseDTO dto);

    Result<Page<Purchase>> list(Long userId, Integer status, String keyword,
                                String startDate, String endDate, int page, int size);

    Result<?> confirm(Long id);

    Result<?> complete(Long id);
}
