package com.gongxiang.zhenxuan.delivery.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 配送订单表
 */
@Data
@TableName("tb_delivery_order")
public class DeliveryOrder {
    
    /**
     * 配送订单ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 主订单ID
     */
    private Long orderId;
    
    /**
     * 配送订单编号
     */
    private String deliveryOrderNo;
    
    /**
     * 骑手ID
     */
    private Long riderId;
    
    /**
     * 取餐地址
     */
    private String pickupAddress;
    
    /**
     * 取餐联系人
     */
    private String pickupContact;
    
    /**
     * 取餐电话
     */
    private String pickupPhone;
    
    /**
     * 取餐地址经度
     */
    private BigDecimal pickupLongitude;
    
    /**
     * 取餐地址纬度
     */
    private BigDecimal pickupLatitude;
    
    /**
     * 配送地址
     */
    private String deliveryAddress;
    
    /**
     * 配送联系人
     */
    private String deliveryContact;
    
    /**
     * 配送电话
     */
    private String deliveryPhone;
    
    /**
     * 配送地址经度
     */
    private BigDecimal deliveryLongitude;
    
    /**
     * 配送地址纬度
     */
    private BigDecimal deliveryLatitude;
    
    /**
     * 配送距离(公里)
     */
    private BigDecimal distance;
    
    /**
     * 配送费
     */
    private BigDecimal deliveryFee;
    
    /**
     * 预计配送时长(分钟)
     */
    private Integer estimatedTime;
    
    /**
     * 实际配送时长(分钟)
     */
    private Integer actualTime;
    
    /**
     * 订单状态：1-待抢单，2-待取餐，3-骑手已到店，4-已取餐，5-配送中，6-已送达，7-已完成，8-已取消
     */
    private Integer status;
    
    /**
     * 抢单截止时间
     */
    private LocalDateTime grabDeadline;
    
    /**
     * 抢单时间
     */
    private LocalDateTime grabTime;
    
    /**
     * 到店时间
     */
    private LocalDateTime arriveTime;
    
    /**
     * 取餐时间
     */
    private LocalDateTime pickupTime;
    
    /**
     * 配送开始时间
     */
    private LocalDateTime deliveryStartTime;
    
    /**
     * 送达时间
     */
    private LocalDateTime deliveredTime;
    
    /**
     * 完成时间
     */
    private LocalDateTime completedTime;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
