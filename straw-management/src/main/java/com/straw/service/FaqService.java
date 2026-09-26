package com.straw.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.straw.common.Result;
import com.straw.dto.FaqDTO;
import com.straw.entity.Faq;

public interface FaqService {

    Result<?> listGroupedByCategory();

    Result<Page<Faq>> listAll(int page, int size);

    Result<Faq> getById(Long id);

    Result<?> create(FaqDTO dto);

    Result<?> update(Long id, FaqDTO dto);

    Result<?> delete(Long id);
}
