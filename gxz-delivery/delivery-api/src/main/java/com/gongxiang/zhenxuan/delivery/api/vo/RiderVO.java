package com.gongxiang.zhenxuan.delivery.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 骑手信息VO
 */
@Data
@ApiModel("骑手信息VO")
public class RiderVO {
    
    @ApiModelProperty("骑手ID")
    private Long id;
    
    @ApiModelProperty("骑手编号")
    private String riderCode;
    
    @ApiModelProperty("手机号")
    private String phone;
    
    @ApiModelProperty("姓名")
    private String name;
    
    @ApiModelProperty("头像")
    private String avatar;
    
    @ApiModelProperty("车辆类型：1-电动车，2-摩托车，3-汽车")
    private Integer vehicleType;
    
    @ApiModelProperty("车牌号")
    private String vehicleNo;
    
    @ApiModelProperty("服务区域")
    private String serviceArea;
    
    @ApiModelProperty("当前位置经度")
    private BigDecimal currentLongitude;
    
    @ApiModelProperty("当前位置纬度")
    private BigDecimal currentLatitude;
    
    @ApiModelProperty("在线状态：1-在线，2-忙碌，3-离线")
    private Integer onlineStatus;
    
    @ApiModelProperty("最大并发订单数")
    private Integer maxConcurrentOrders;
    
    @ApiModelProperty("当前配送订单数")
    private Integer currentOrdersCount;
    
    @ApiModelProperty("服务范围(公里)")
    private BigDecimal serviceRange;
    
    @ApiModelProperty("评分")
    private BigDecimal rating;
    
    @ApiModelProperty("总配送单数")
    private Integer totalOrders;
    
    @ApiModelProperty("完成配送单数")
    private Integer completedOrders;
    
    @ApiModelProperty("账户状态：1-正常，2-冻结，3-停用")
    private Integer status;
    
    @ApiModelProperty("注册时间")
    private LocalDateTime registerTime;
    
    @ApiModelProperty("最后上线时间")
    private LocalDateTime lastOnlineTime;
}
