package com.gongxiang.zhenxuan.delivery.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 配送订单状态枚举
 */
@Getter
@AllArgsConstructor
public enum DeliveryOrderStatus {
    
    /**
     * 待抢单
     */
    WAIT_GRAB(1, "待抢单"),
    
    /**
     * 待取餐
     */
    WAIT_PICKUP(2, "待取餐"),
    
    /**
     * 骑手已到店
     */
    RIDER_ARRIVED(3, "骑手已到店"),
    
    /**
     * 已取餐
     */
    PICKED_UP(4, "已取餐"),
    
    /**
     * 配送中
     */
    DELIVERING(5, "配送中"),
    
    /**
     * 已送达
     */
    DELIVERED(6, "已送达"),
    
    /**
     * 已完成
     */
    COMPLETED(7, "已完成"),
    
    /**
     * 已取消
     */
    CANCELLED(8, "已取消");
    
    private final Integer code;
    private final String desc;
    
    public static DeliveryOrderStatus getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (DeliveryOrderStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
    
    /**
     * 检查状态转换是否合法
     */
    public static boolean isValidTransition(DeliveryOrderStatus from, DeliveryOrderStatus to) {
        if (from == null || to == null) {
            return false;
        }
        
        switch (from) {
            case WAIT_GRAB:
                return to == WAIT_PICKUP || to == CANCELLED;
            case WAIT_PICKUP:
                return to == RIDER_ARRIVED || to == CANCELLED;
            case RIDER_ARRIVED:
                return to == PICKED_UP || to == CANCELLED;
            case PICKED_UP:
                return to == DELIVERING || to == CANCELLED;
            case DELIVERING:
                return to == DELIVERED || to == CANCELLED;
            case DELIVERED:
                return to == COMPLETED;
            case COMPLETED:
            case CANCELLED:
                return false;
            default:
                return false;
        }
    }
}
