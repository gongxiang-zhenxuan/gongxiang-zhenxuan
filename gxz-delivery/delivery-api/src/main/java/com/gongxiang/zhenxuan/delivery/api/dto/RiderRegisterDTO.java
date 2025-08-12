package com.gongxiang.zhenxuan.delivery.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 骑手注册DTO
 */
@Data
@ApiModel("骑手注册DTO")
public class RiderRegisterDTO {
    
    @ApiModelProperty("微信openId")
    private String openId;
    
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @ApiModelProperty(value = "手机号", required = true)
    private String phone;
    
    @NotBlank(message = "姓名不能为空")
    @ApiModelProperty(value = "姓名", required = true)
    private String name;
    
    @ApiModelProperty("身份证号")
    private String idCard;
    
    @ApiModelProperty("头像")
    private String avatar;
    
    @NotNull(message = "车辆类型不能为空")
    @ApiModelProperty(value = "车辆类型：1-电动车，2-摩托车，3-汽车", required = true)
    private Integer vehicleType;
    
    @ApiModelProperty("车牌号")
    private String vehicleNo;
    
    @ApiModelProperty("服务区域")
    private String serviceArea;
}
