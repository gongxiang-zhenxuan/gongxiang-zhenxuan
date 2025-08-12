package com.gongxiang.zhenxuan.delivery.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 骑手信息表
 */
@Data
@TableName("tb_rider")
public class Rider {
    
    /**
     * 骑手ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 骑手编号
     */
    private String riderCode;
    
    /**
     * 微信openId
     */
    private String openId;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 姓名
     */
    private String name;
    
    /**
     * 身份证号
     */
    private String idCard;
    
    /**
     * 头像
     */
    private String avatar;
    
    /**
     * 车辆类型：1-电动车，2-摩托车，3-汽车
     */
    private Integer vehicleType;
    
    /**
     * 车牌号
     */
    private String vehicleNo;
    
    /**
     * 服务区域
     */
    private String serviceArea;
    
    /**
     * 当前位置经度
     */
    @TableField("current_longitude")
    private BigDecimal currentLongitude;
    
    /**
     * 当前位置纬度
     */
    @TableField("current_latitude") 
    private BigDecimal currentLatitude;
    
    /**
     * 在线状态：1-在线，2-忙碌，3-离线
     */
    private Integer onlineStatus;
    
    /**
     * 最大并发订单数
     */
    private Integer maxConcurrentOrders;
    
    /**
     * 当前配送订单数
     */
    private Integer currentOrdersCount;
    
    /**
     * 服务范围(公里)
     */
    private BigDecimal serviceRange;
    
    /**
     * 评分
     */
    private BigDecimal rating;
    
    /**
     * 总配送单数
     */
    private Integer totalOrders;
    
    /**
     * 完成配送单数
     */
    private Integer completedOrders;
    
    /**
     * 账户状态：1-正常，2-冻结，3-停用
     */
    private Integer status;
    
    /**
     * 注册时间
     */
    private LocalDateTime registerTime;
    
    /**
     * 最后上线时间
     */
    private LocalDateTime lastOnlineTime;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
