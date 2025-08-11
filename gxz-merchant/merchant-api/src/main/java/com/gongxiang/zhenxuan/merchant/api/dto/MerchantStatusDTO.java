package com.gongxiang.zhenxuan.merchant.api.dto;

import javax.validation.constraints.NotNull;

/**
 * 商户状态更新DTO
 *
 * @author gongxiang-zhenxuan
 */
public class MerchantStatusDTO {

    @NotNull(message = "状态不能为空")
    private Integer status;

    private String reason;

    public MerchantStatusDTO() {
    }

    public MerchantStatusDTO(Integer status, String reason) {
        this.status = status;
        this.reason = reason;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "MerchantStatusDTO{" +
                "status=" + status +
                ", reason='" + reason + '\'' +
                '}';
    }
}