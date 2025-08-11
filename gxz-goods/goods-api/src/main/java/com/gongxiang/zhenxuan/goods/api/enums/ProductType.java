package com.gongxiang.zhenxuan.goods.api.enums;

/**
 * 商品类型枚举
 *
 * @author gongxiang-zhenxuan
 */
public enum ProductType {

    TAKEAWAY(1, "外卖菜品"),
    PLATFORM(2, "平台自营商品"),
    GROUPBUY(3, "团购商品");

    private final Integer code;
    private final String description;

    ProductType(Integer code, String description) {
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
     * 根据类型码获取枚举
     */
    public static ProductType getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ProductType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    /**
     * 获取类型描述
     */
    public static String getDescription(Integer code) {
        ProductType type = getByCode(code);
        return type != null ? type.getDescription() : "未知类型";
    }
}