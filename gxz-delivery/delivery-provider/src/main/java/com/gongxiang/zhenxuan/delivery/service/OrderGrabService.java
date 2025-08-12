package com.gongxiang.zhenxuan.delivery.service;

import com.gongxiang.zhenxuan.common.result.Result;
import com.gongxiang.zhenxuan.delivery.api.dto.OrderGrabDTO;
import com.gongxiang.zhenxuan.delivery.api.dto.OrderStatusUpdateDTO;
import com.gongxiang.zhenxuan.delivery.api.vo.DeliveryOrderVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单抢单服务接口
 */
public interface OrderGrabService {
    
    /**
     * 查询附近可抢订单
     */
    Result<List<DeliveryOrderVO>> findNearbyGrabbableOrders(BigDecimal riderLng, BigDecimal riderLat, BigDecimal serviceRange);
    
    /**
     * 骑手抢单
     */
    Result<String> grabOrder(OrderGrabDTO dto);
    
    /**
     * 更新配送状态
     */
    Result<String> updateDeliveryStatus(OrderStatusUpdateDTO dto);
}
