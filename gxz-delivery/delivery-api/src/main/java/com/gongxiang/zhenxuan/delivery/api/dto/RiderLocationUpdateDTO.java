package com.gongxiang.zhenxuan.delivery.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 骑手位置更新DTO
 */
@Data
@ApiModel("骑手位置更新DTO")
public class RiderLocationUpdateDTO {
    
    @NotNull(message = "骑手ID不能为空")
    @ApiModelProperty(value = "骑手ID", required = true)
    private Long riderId;
    
    @NotNull(message = "经度不能为空")
    @ApiModelProperty(value = "经度", required = true)
    private BigDecimal longitude;
    
    @NotNull(message = "纬度不能为空")
    @ApiModelProperty(value = "纬度", required = true)
    private BigDecimal latitude;
    
    @ApiModelProperty("精度(米)")
    private Integer accuracy;
    
    @ApiModelProperty("速度(km/h)")
    private BigDecimal speed;
    
    @ApiModelProperty("方向角度")
    private BigDecimal direction;
}
