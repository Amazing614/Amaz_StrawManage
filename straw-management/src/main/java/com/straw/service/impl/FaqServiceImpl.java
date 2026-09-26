package com.straw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.straw.common.Result;
import com.straw.dto.FaqDTO;
import com.straw.entity.Faq;
import com.straw.mapper.FaqMapper;
import com.straw.service.FaqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FaqServiceImpl implements FaqService {

    @Autowired
    private FaqMapper faqMapper;

    @Override
    public Result<?> listGroupedByCategory() {
        List<Faq> faqs = faqMapper.selectList(
                new LambdaQueryWrapper<Faq>()
                        .eq(Faq::getStatus, 1)
                        .orderByAsc(Faq::getSortOrder));
        Map<String, List<Faq>> grouped = faqs.stream()
                .collect(Collectors.groupingBy(Faq::getCategory, LinkedHashMap::new, Collectors.toList()));
        return Result.success(grouped);
    }

    @Override
    public Result<Page<Faq>> listAll(int page, int size) {
        Page<Faq> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Faq> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Faq::getSortOrder);
        Page<Faq> result = faqMapper.selectPage(pageParam, wrapper);
        return Result.success(result);
    }

    @Override
    public Result<Faq> getById(Long id) {
        Faq faq = faqMapper.selectById(id);
        if (faq == null) {
            return Result.error("FAQ不存在");
        }
        return Result.success(faq);
    }

    @Override
    public Result<?> create(FaqDTO dto) {
        Faq faq = new Faq();
        faq.setQuestion(dto.getQuestion());
        faq.setAnswer(dto.getAnswer());
        faq.setCategory(dto.getCategory() != null ? dto.getCategory() : "通用");
        faq.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);
        faq.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        faqMapper.insert(faq);
        return Result.success("创建成功", null);
    }

    @Override
    public Result<?> update(Long id, FaqDTO dto) {
        Faq faq = faqMapper.selectById(id);
        if (faq == null) {
            return Result.error("FAQ不存在");
        }
        faq.setQuestion(dto.getQuestion());
        faq.setAnswer(dto.getAnswer());
        if (dto.getCategory() != null) {
            faq.setCategory(dto.getCategory());
        }
        if (dto.getSortOrder() != null) {
            faq.setSortOrder(dto.getSortOrder());
        }
        if (dto.getStatus() != null) {
            faq.setStatus(dto.getStatus());
        }
        faqMapper.updateById(faq);
        return Result.success("更新成功", null);
    }

    @Override
    public Result<?> delete(Long id) {
        Faq faq = faqMapper.selectById(id);
        if (faq == null) {
            return Result.error("FAQ不存在");
        }
        faqMapper.deleteById(id);
        return Result.success("删除成功", null);
    }
}
