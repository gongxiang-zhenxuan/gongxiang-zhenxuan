package com.gongxiang.zhenxuan.delivery.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 骑手状态更新DTO
 */
@Data
@ApiModel("骑手状态更新DTO")
public class RiderStatusUpdateDTO {
    
    @NotNull(message = "骑手ID不能为空")
    @ApiModelProperty(value = "骑手ID", required = true)
    private Long riderId;
    
    @NotNull(message = "在线状态不能为空")
    @ApiModelProperty(value = "在线状态：1-在线，2-忙碌，3-离线", required = true)
    private Integer onlineStatus;
    
    @ApiModelProperty("经度")
    private BigDecimal longitude;
    
    @ApiModelProperty("纬度")
    private BigDecimal latitude;
}
