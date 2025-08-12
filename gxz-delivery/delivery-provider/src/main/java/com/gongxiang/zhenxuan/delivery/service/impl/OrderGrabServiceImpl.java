package com.gongxiang.zhenxuan.delivery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gongxiang.zhenxuan.common.result.Result;
import com.gongxiang.zhenxuan.common.result.ResultCode;
import com.gongxiang.zhenxuan.delivery.api.dto.OrderGrabDTO;
import com.gongxiang.zhenxuan.delivery.api.dto.OrderStatusUpdateDTO;
import com.gongxiang.zhenxuan.delivery.api.enums.DeliveryOrderStatus;
import com.gongxiang.zhenxuan.delivery.api.vo.DeliveryOrderVO;
import com.gongxiang.zhenxuan.delivery.entity.DeliveryOrder;
import com.gongxiang.zhenxuan.delivery.entity.Rider;
import com.gongxiang.zhenxuan.delivery.mapper.DeliveryOrderMapper;
import com.gongxiang.zhenxuan.delivery.service.DeliveryOrderService;
import com.gongxiang.zhenxuan.delivery.service.OrderGrabService;
import com.gongxiang.zhenxuan.delivery.service.RiderService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 订单抢单服务实现
 */
@Slf4j
@Service
public class OrderGrabServiceImpl implements OrderGrabService {

    @Autowired
    private DeliveryOrderMapper deliveryOrderMapper;
    
    @Autowired
    private DeliveryOrderService deliveryOrderService;
    
    @Autowired
    private RiderService riderService;
    
    @Autowired
    private RedissonClient redissonClient;
    
    private static final String ORDER_GRAB_LOCK_PREFIX = "order:grab:lock:";
    
    @Override
    public Result<List<DeliveryOrderVO>> findNearbyGrabbableOrders(BigDecimal riderLng, BigDecimal riderLat, 
                                                                  BigDecimal serviceRange) {
        try {
            // 计算经纬度范围
            BigDecimal lngDelta = serviceRange.divide(new BigDecimal("111.320"), 6, RoundingMode.HALF_UP);
            BigDecimal latDelta = serviceRange.divide(new BigDecimal("110.540"), 6, RoundingMode.HALF_UP);
            
            BigDecimal minLng = riderLng.subtract(lngDelta);
            BigDecimal maxLng = riderLng.add(lngDelta);
            BigDecimal minLat = riderLat.subtract(latDelta);
            BigDecimal maxLat = riderLat.add(latDelta);
            
            // 查询可抢订单
            LambdaQueryWrapper<DeliveryOrder> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DeliveryOrder::getStatus, DeliveryOrderStatus.WAIT_GRAB.getCode())
                        .isNull(DeliveryOrder::getRiderId)
                        .gt(DeliveryOrder::getGrabDeadline, LocalDateTime.now())
                        .between(DeliveryOrder::getPickupLongitude, minLng, maxLng)
                        .between(DeliveryOrder::getPickupLatitude, minLat, maxLat)
                        .orderByDesc(DeliveryOrder::getDeliveryFee)
                        .orderByAsc(DeliveryOrder::getCreateTime)
                        .last("LIMIT 20");
            
            List<DeliveryOrder> orders = deliveryOrderMapper.selectList(queryWrapper);
            
            List<DeliveryOrderVO> result = orders.stream()
                    .map(order -> {
                        DeliveryOrderVO vo = new DeliveryOrderVO();
                        BeanUtils.copyProperties(order, vo);
                        
                        // 计算距离商家的距离
                        BigDecimal shopDistance = calculateDistance(riderLng, riderLat, 
                                                                  order.getPickupLongitude(), order.getPickupLatitude());
                        vo.setShopDistance(shopDistance);
                        
                        return vo;
                    })
                    .collect(Collectors.toList());
            
            return Result.success(result);
            
        } catch (Exception e) {
            log.error("查询附近可抢订单失败", e);
            return Result.fail("查询附近订单失败");
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> grabOrder(OrderGrabDTO dto) {
        String lockKey = ORDER_GRAB_LOCK_PREFIX + dto.getOrderId();
        RLock lock = redissonClient.getLock(lockKey);
        
        try {
            // 尝试获取锁，等待最多3秒，锁最多持有10秒
            if (lock.tryLock(3, 10, TimeUnit.SECONDS)) {
                try {
                    // 验证骑手状态
                    Result<Rider> riderResult = getRiderAndValidate(dto.getRiderId());
                    if (riderResult.getCode() != 200) {
                        return Result.fail(riderResult.getMessage());
                    }
                    Rider rider = riderResult.getData();
                    
                    // 检查订单状态
                    DeliveryOrder order = deliveryOrderService.getById(dto.getOrderId());
                    if (order == null) {
                        return Result.fail("订单不存在");
                    }
                    
                    if (!DeliveryOrderStatus.WAIT_GRAB.getCode().equals(order.getStatus())) {
                        return Result.fail("订单已被抢取或状态异常");
                    }
                    
                    if (order.getRiderId() != null) {
                        return Result.fail("订单已被其他骑手抢取");
                    }
                    
                    if (order.getGrabDeadline().isBefore(LocalDateTime.now())) {
                        return Result.fail("订单已过抢单时间");
                    }
                    
                    // 检查骑手当前订单数
                    if (rider.getCurrentOrdersCount() >= rider.getMaxConcurrentOrders()) {
                        return Result.fail("当前订单数已达上限");
                    }
                    
                    // 更新订单状态
                    LambdaUpdateWrapper<DeliveryOrder> orderUpdateWrapper = new LambdaUpdateWrapper<>();
                    orderUpdateWrapper.eq(DeliveryOrder::getId, dto.getOrderId())
                                    .isNull(DeliveryOrder::getRiderId)
                                    .set(DeliveryOrder::getRiderId, dto.getRiderId())
                                    .set(DeliveryOrder::getStatus, DeliveryOrderStatus.WAIT_PICKUP.getCode())
                                    .set(DeliveryOrder::getGrabTime, LocalDateTime.now());
                    
                    boolean orderUpdated = deliveryOrderService.update(orderUpdateWrapper);
                    if (!orderUpdated) {
                        return Result.fail("抢单失败，订单可能已被抢取");
                    }
                    
                    // 更新骑手订单计数
                    riderService.updateOrderCount(dto.getRiderId(), 1);
                    
                    log.info("抢单成功，riderId：{}，orderId：{}", dto.getRiderId(), dto.getOrderId());
                    return Result.success("抢单成功");
                    
                } finally {
                    lock.unlock();
                }
            } else {
                return Result.fail("抢单失败，请稍后重试");
            }
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("抢单被中断", e);
            return Result.fail("抢单失败");
        } catch (Exception e) {
            log.error("抢单失败", e);
            return Result.fail("抢单失败");
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> updateDeliveryStatus(OrderStatusUpdateDTO dto) {
        try {
            DeliveryOrder order = deliveryOrderService.getById(dto.getOrderId());
            if (order == null) {
                return Result.fail("订单不存在");
            }
            
            // 验证状态转换的合法性
            if (!isValidStatusTransition(order.getStatus(), dto.getTargetStatus())) {
                return Result.fail("无效的状态转换");
            }
            
            LambdaUpdateWrapper<DeliveryOrder> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(DeliveryOrder::getId, dto.getOrderId())
                        .set(DeliveryOrder::getStatus, dto.getTargetStatus());
            
            // 根据目标状态设置相应的时间戳
            switch (DeliveryOrderStatus.getByCode(dto.getTargetStatus())) {
                case PICKED_UP:
                    updateWrapper.set(DeliveryOrder::getPickupTime, LocalDateTime.now());
                    break;
                case DELIVERING:
                    updateWrapper.set(DeliveryOrder::getDeliveryStartTime, LocalDateTime.now());
                    break;
                case COMPLETED:
                    updateWrapper.set(DeliveryOrder::getCompletedTime, LocalDateTime.now());
                    // 订单完成时减少骑手当前订单数
                    riderService.updateOrderCount(order.getRiderId(), -1);
                    break;
                case CANCELLED:
                    updateWrapper.set(DeliveryOrder::getCompletedTime, LocalDateTime.now());
                    if (dto.getRemark() != null) {
                        updateWrapper.set(DeliveryOrder::getRemark, dto.getRemark());
                    }
                    // 订单取消时减少骑手当前订单数
                    riderService.updateOrderCount(order.getRiderId(), -1);
                    break;
            }
            
            if (dto.getRemark() != null) {
                updateWrapper.set(DeliveryOrder::getRemark, dto.getRemark());
            }
            
            deliveryOrderService.update(updateWrapper);
            
            log.info("配送状态更新成功，orderId：{}，状态：{}", dto.getOrderId(), dto.getTargetStatus());
            return Result.success("状态更新成功");
            
        } catch (Exception e) {
            log.error("更新配送状态失败", e);
            return Result.fail("状态更新失败");
        }
    }
    
    /**
     * 获取并验证骑手信息
     */
    private Result<Rider> getRiderAndValidate(Long riderId) {
        try {
            Result<com.gongxiang.zhenxuan.delivery.api.vo.RiderVO> riderResult = riderService.getRiderInfo(riderId);
            if (riderResult.getCode() != 200) {
                return Result.fail("骑手不存在");
            }
            
            com.gongxiang.zhenxuan.delivery.api.vo.RiderVO riderVO = riderResult.getData();
            if (riderVO.getStatus() != 1) {
                return Result.fail("骑手账户状态异常");
            }
            
            if (riderVO.getOnlineStatus() != 1) {
                return Result.fail("骑手未在线");
            }
            
            // 转换为实体对象
            Rider rider = new Rider();
            BeanUtils.copyProperties(riderVO, rider);
            
            return Result.success(rider);
            
        } catch (Exception e) {
            log.error("验证骑手信息失败，riderId：{}", riderId, e);
            return Result.fail("验证骑手信息失败");
        }
    }
    
    /**
     * 计算两点间距离（公里）
     */
    private BigDecimal calculateDistance(BigDecimal lng1, BigDecimal lat1, BigDecimal lng2, BigDecimal lat2) {
        final double EARTH_RADIUS = 6371.0; // 地球半径（公里）
        
        double radLat1 = Math.toRadians(lat1.doubleValue());
        double radLat2 = Math.toRadians(lat2.doubleValue());
        double deltaLat = Math.toRadians(lat2.subtract(lat1).doubleValue());
        double deltaLng = Math.toRadians(lng2.subtract(lng1).doubleValue());
        
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                   Math.cos(radLat1) * Math.cos(radLat2) *
                   Math.sin(deltaLng / 2) * Math.sin(deltaLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        double distance = EARTH_RADIUS * c;
        return new BigDecimal(distance).setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * 验证状态转换的合法性
     */
    private boolean isValidStatusTransition(Integer currentStatus, Integer targetStatus) {
        DeliveryOrderStatus current = DeliveryOrderStatus.getByCode(currentStatus);
        DeliveryOrderStatus target = DeliveryOrderStatus.getByCode(targetStatus);
        
        if (current == null || target == null) {
            return false;
        }
        
        // 定义合法的状态转换
        switch (current) {
            case WAIT_PICKUP:
                return target == DeliveryOrderStatus.PICKED_UP || target == DeliveryOrderStatus.CANCELLED;
            case PICKED_UP:
                return target == DeliveryOrderStatus.DELIVERING || target == DeliveryOrderStatus.CANCELLED;
            case DELIVERING:
                return target == DeliveryOrderStatus.COMPLETED || target == DeliveryOrderStatus.CANCELLED;
            default:
                return false;
        }
    }
}