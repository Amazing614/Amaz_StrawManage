package com.straw.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.straw.common.Result;
import com.straw.dto.FaqDTO;
import com.straw.entity.Faq;
import com.straw.service.FaqService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class FaqController {

    @Autowired
    private FaqService faqService;

    // 用户端：获取启用的FAQ按分类分组
    @GetMapping("/faq/categories")
    public Result<?> listCategories() {
        return faqService.listGroupedByCategory();
    }

    // 管理端：FAQ列表
    @GetMapping("/admin/faq/list")
    public Result<Page<Faq>> listAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return faqService.listAll(page, size);
    }

    // 管理端：新增FAQ
    @PostMapping("/admin/faq")
    public Result<?> create(@Valid @RequestBody FaqDTO dto) {
        return faqService.create(dto);
    }

    // 管理端：修改FAQ
    @PutMapping("/admin/faq/{id}")
    public Result<?> update(@PathVariable Long id, @Valid @RequestBody FaqDTO dto) {
        return faqService.update(id, dto);
    }

    // 管理端：删除FAQ
    @DeleteMapping("/admin/faq/{id}")
    public Result<?> delete(@PathVariable Long id) {
        return faqService.delete(id);
    }
}
