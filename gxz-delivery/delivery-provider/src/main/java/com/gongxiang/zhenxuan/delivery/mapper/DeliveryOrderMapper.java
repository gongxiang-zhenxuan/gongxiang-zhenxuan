package com.gongxiang.zhenxuan.delivery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gongxiang.zhenxuan.delivery.entity.DeliveryOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * 配送订单Mapper接口
 */
@Mapper
public interface DeliveryOrderMapper extends BaseMapper<DeliveryOrder> {
    
}
