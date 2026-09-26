package com.straw.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.straw.common.Result;
import com.straw.dto.StrawInfoDTO;
import com.straw.entity.StrawInfo;

public interface StrawInfoService {

    Result<?> report(Long userId, StrawInfoDTO dto);

    Result<Page<StrawInfo>> list(Long userId, Integer status, int page, int size);

    Result<StrawInfo> getById(Long id);

    Result<?> update(Long id, Long userId, StrawInfoDTO dto);

    Result<?> delete(Long id, Long userId);

    Result<?> audit(Long id, Integer status, String remark);
}
