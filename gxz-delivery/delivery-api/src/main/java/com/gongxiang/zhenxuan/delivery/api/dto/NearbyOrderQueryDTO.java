package com.gongxiang.zhenxuan.delivery.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 附近订单查询DTO - 支持骑手个性化筛选条件
 */
@Data
@ApiModel("附近订单查询DTO")
public class NearbyOrderQueryDTO {
    
    @NotNull(message = "骑手ID不能为空")
    @ApiModelProperty(value = "骑手ID", required = true)
    private Long riderId;
    
    @NotNull(message = "经度不能为空")
    @ApiModelProperty(value = "骑手当前经度", required = true)
    private BigDecimal longitude;
    
    @NotNull(message = "纬度不能为空")
    @ApiModelProperty(value = "骑手当前纬度", required = true)
    private BigDecimal latitude;
    
    @ApiModelProperty("服务范围(公里)，默认5.0")
    private BigDecimal serviceRange;
    
    @ApiModelProperty("最低配送费筛选，单位元")
    private BigDecimal minDeliveryFee;
    
    @ApiModelProperty("最高配送费筛选，单位元")
    private BigDecimal maxDeliveryFee;
    
    @ApiModelProperty("偏好商户ID列表")
    private List<Long> preferredMerchantIds;
    
    @ApiModelProperty("排序方式：1-配送费降序，2-距离升序，3-订单时间升序，默认1")
    private Integer sortType;
    
    @ApiModelProperty("是否只显示熟悉商户的订单，默认false")
    private Boolean onlyFamiliarMerchants;
    
    @ApiModelProperty("最大返回订单数量，默认20")
    private Integer maxResults;
}
