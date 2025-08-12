package com.gongxiang.zhenxuan.delivery.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 订单抢单DTO
 */
@Data
@ApiModel("订单抢单DTO")
public class OrderGrabDTO {
    
    @NotNull(message = "订单ID不能为空")
    @ApiModelProperty(value = "订单ID", required = true)
    private Long orderId;
    
    @NotNull(message = "骑手ID不能为空")
    @ApiModelProperty(value = "骑手ID", required = true)
    private Long riderId;
}
