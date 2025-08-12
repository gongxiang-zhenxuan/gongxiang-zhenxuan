package com.gongxiang.zhenxuan.delivery.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 附近订单查询DTO
 */
@Data
@ApiModel("附近订单查询DTO")
public class NearbyOrderQueryDTO {
    
    @NotNull(message = "经度不能为空")
    @ApiModelProperty(value = "经度", required = true)
    private BigDecimal longitude;
    
    @NotNull(message = "纬度不能为空")
    @ApiModelProperty(value = "纬度", required = true)
    private BigDecimal latitude;
    
    @ApiModelProperty("服务范围(公里)，默认5公里")
    private BigDecimal serviceRange;
}
