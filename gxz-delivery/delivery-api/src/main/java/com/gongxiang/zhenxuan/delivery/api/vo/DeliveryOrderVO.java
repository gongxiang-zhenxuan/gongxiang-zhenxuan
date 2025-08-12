package com.gongxiang.zhenxuan.delivery.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 配送订单VO
 */
@Data
@ApiModel("配送订单VO")
public class DeliveryOrderVO {
    
    @ApiModelProperty("配送订单ID")
    private Long id;
    
    @ApiModelProperty("主订单ID")
    private Long orderId;
    
    @ApiModelProperty("配送订单编号")
    private String deliveryOrderNo;
    
    @ApiModelProperty("骑手ID")
    private Long riderId;
    
    @ApiModelProperty("取餐地址")
    private String pickupAddress;
    
    @ApiModelProperty("取餐联系人")
    private String pickupContact;
    
    @ApiModelProperty("取餐电话")
    private String pickupPhone;
    
    @ApiModelProperty("取餐地址经度")
    private BigDecimal pickupLongitude;
    
    @ApiModelProperty("取餐地址纬度")
    private BigDecimal pickupLatitude;
    
    @ApiModelProperty("配送地址")
    private String deliveryAddress;
    
    @ApiModelProperty("配送联系人")
    private String deliveryContact;
    
    @ApiModelProperty("配送电话")
    private String deliveryPhone;
    
    @ApiModelProperty("配送地址经度")
    private BigDecimal deliveryLongitude;
    
    @ApiModelProperty("配送地址纬度")
    private BigDecimal deliveryLatitude;
    
    @ApiModelProperty("配送距离(公里)")
    private BigDecimal distance;
    
    @ApiModelProperty("配送费")
    private BigDecimal deliveryFee;
    
    @ApiModelProperty("预计配送时长(分钟)")
    private Integer estimatedTime;
    
    @ApiModelProperty("实际配送时长(分钟)")
    private Integer actualTime;
    
    @ApiModelProperty("订单状态")
    private Integer status;
    
    @ApiModelProperty("抢单截止时间")
    private LocalDateTime grabDeadline;
    
    @ApiModelProperty("距离商家距离(公里)")
    private BigDecimal shopDistance;
    
    @ApiModelProperty("备注")
    private String remark;
    
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
