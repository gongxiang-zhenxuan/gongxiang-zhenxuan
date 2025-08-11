package com.gongxiang.zhenxuan.goods.api.enums;

/**
 * 商品状态枚举
 *
 * @author gongxiang-zhenxuan
 */
public enum ProductStatus {

    ON_SHELF(1, "上架"),
    OFF_SHELF(2, "下架"),
    OUT_OF_STOCK(3, "缺货");

    private final Integer code;
    private final String description;

    ProductStatus(Integer code, String description) {
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
    public static ProductStatus getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ProductStatus status : values()) {
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
        ProductStatus status = getByCode(code);
        return status != null ? status.getDescription() : "未知状态";
    }
}