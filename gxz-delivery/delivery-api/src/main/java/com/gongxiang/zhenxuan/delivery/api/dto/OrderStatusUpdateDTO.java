package com.gongxiang.zhenxuan.delivery.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 订单状态更新DTO
 */
@Data
@ApiModel("订单状态更新DTO")
public class OrderStatusUpdateDTO {
    
    @NotNull(message = "订单ID不能为空")
    @ApiModelProperty(value = "订单ID", required = true)
    private Long orderId;
    
    @NotNull(message = "骑手ID不能为空")
    @ApiModelProperty(value = "骑手ID", required = true)
    private Long riderId;
    
    @NotNull(message = "目标状态不能为空")
    @ApiModelProperty(value = "目标状态", required = true)
    private Integer targetStatus;
    
    @ApiModelProperty("经度")
    private BigDecimal longitude;
    
    @ApiModelProperty("纬度")
    private BigDecimal latitude;
    
    @ApiModelProperty("备注")
    private String remark;
}
