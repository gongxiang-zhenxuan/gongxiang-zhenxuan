package com.gongxiang.zhenxuan.merchant.api.enums;

/**
 * 商户状态枚举
 *
 * @author gongxiang-zhenxuan
 */
public enum MerchantStatus {

    PENDING(0, "待审核"),
    APPROVED(1, "营业中"),
    SUSPENDED(2, "休息中"),
    REJECTED(3, "审核拒绝"),
    DISABLED(4, "已禁用");

    private final Integer code;
    private final String description;

    MerchantStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据状态码获取枚举
     */
    public static MerchantStatus getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (MerchantStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

    /**
     * 获取状态描述
     */
    public static String getDescription(Integer code) {
        MerchantStatus status = getByCode(code);
        return status != null ? status.getDescription() : "未知状态";
    }
}