package com.straw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.straw.entity.Purchase;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PurchaseMapper extends BaseMapper<Purchase> {
}
