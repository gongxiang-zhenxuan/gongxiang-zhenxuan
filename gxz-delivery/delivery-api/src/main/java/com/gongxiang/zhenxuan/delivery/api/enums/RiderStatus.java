package com.gongxiang.zhenxuan.delivery.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 骑手状态枚举
 */
@Getter
@AllArgsConstructor
public enum RiderStatus {
    
    /**
     * 正常
     */
    NORMAL(1, "正常"),
    
    /**
     * 冻结
     */
    FROZEN(2, "冻结"),
    
    /**
     * 停用
     */
    DISABLED(3, "停用");
    
    private final Integer code;
    private final String desc;
    
    public static RiderStatus getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (RiderStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
