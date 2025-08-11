package com.gongxiang.zhenxuan.common.result;

/**
 * 响应状态码枚举
 *
 * @author gongxiang-zhenxuan
 */
public enum ResultCode {

    // 通用状态码
    SUCCESS(200, "操作成功"),
    ERROR(500, "操作失败"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),
    TOO_MANY_REQUESTS(429, "请求过于频繁"),

    // 用户相关状态码 1xxx
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_ALREADY_EXISTS(1002, "用户已存在"),
    USER_PASSWORD_ERROR(1003, "密码错误"),
    USER_ACCOUNT_DISABLED(1004, "账户已禁用"),
    USER_TOKEN_EXPIRED(1005, "登录已过期"),
    USER_TOKEN_INVALID(1006, "无效的令牌"),

    // 商户相关状态码 2xxx
    MERCHANT_NOT_FOUND(2001, "商户不存在"),
    MERCHANT_NOT_APPROVED(2002, "商户未审核通过"),
    MERCHANT_SUSPENDED(2003, "商户已暂停营业"),
    MERCHANT_PERMISSION_DENIED(2004, "商户权限不足"),

    // 商品相关状态码 3xxx
    PRODUCT_NOT_FOUND(3001, "商品不存在"),
    PRODUCT_STOCK_INSUFFICIENT(3002, "商品库存不足"),
    PRODUCT_OFF_SHELF(3003, "商品已下架"),
    PRODUCT_PRICE_CHANGED(3004, "商品价格已变更"),

    // 订单相关状态码 4xxx
    ORDER_NOT_FOUND(4001, "订单不存在"),
    ORDER_STATUS_ERROR(4002, "订单状态错误"),
    ORDER_CANNOT_CANCEL(4003, "订单无法取消"),
    ORDER_ALREADY_PAID(4004, "订单已支付"),
    ORDER_EXPIRED(4005, "订单已过期"),

    // 支付相关状态码 5xxx
    PAYMENT_FAILED(5001, "支付失败"),
    PAYMENT_AMOUNT_ERROR(5002, "支付金额错误"),
    PAYMENT_ALREADY_PAID(5003, "订单已支付"),
    REFUND_FAILED(5004, "退款失败"),
    REFUND_AMOUNT_ERROR(5005, "退款金额错误"),

    // 配送相关状态码 6xxx
    DELIVERY_NOT_FOUND(6001, "配送信息不存在"),
    RIDER_NOT_FOUND(6002, "骑手不存在"),
    ORDER_ALREADY_GRABBED(6003, "订单已被抢单"),
    RIDER_BUSY(6004, "骑手忙碌中"),
    DELIVERY_AREA_NOT_SUPPORT(6005, "配送区域不支持"),

    // 营销相关状态码 7xxx
    COUPON_NOT_FOUND(7001, "优惠券不存在"),
    COUPON_EXPIRED(7002, "优惠券已过期"),
    COUPON_ALREADY_USED(7003, "优惠券已使用"),
    COUPON_NOT_AVAILABLE(7004, "优惠券不可用"),
    PROMOTION_NOT_ACTIVE(7005, "活动未开始或已结束"),

    // 系统相关状态码 9xxx
    SYSTEM_ERROR(9001, "系统错误"),
    DATABASE_ERROR(9002, "数据库错误"),
    NETWORK_ERROR(9003, "网络错误"),
    EXTERNAL_API_ERROR(9004, "外部接口调用失败"),
    CONFIG_ERROR(9005, "配置错误");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    /**
     * 根据状态码获取枚举
     */
    public static ResultCode getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ResultCode resultCode : values()) {
            if (resultCode.getCode().equals(code)) {
                return resultCode;
            }
        }
        return null;
    }
}