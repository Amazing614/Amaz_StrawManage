package com.straw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.straw.entity.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
