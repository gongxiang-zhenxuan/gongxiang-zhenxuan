package com.gongxiang.zhenxuan.delivery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gongxiang.zhenxuan.common.result.Result;
import com.gongxiang.zhenxuan.delivery.api.dto.RiderLocationUpdateDTO;
import com.gongxiang.zhenxuan.delivery.api.dto.RiderRegisterDTO;
import com.gongxiang.zhenxuan.delivery.api.dto.RiderStatusUpdateDTO;
import com.gongxiang.zhenxuan.delivery.api.vo.RiderVO;
import com.gongxiang.zhenxuan.delivery.entity.Rider;

import java.math.BigDecimal;
import java.util.List;

/**
 * 骑手服务接口
 */
public interface RiderService extends IService<Rider> {
    
    /**
     * 骑手注册
     */
    Result<String> register(RiderRegisterDTO dto);
    
    /**
     * 获取骑手信息
     */
    Result<RiderVO> getRiderInfo(Long riderId);
    
    /**
     * 更新在线状态
     */
    Result<String> updateOnlineStatus(RiderStatusUpdateDTO dto);
    
    /**
     * 更新位置信息
     */
    Result<String> updateLocation(RiderLocationUpdateDTO dto);
    
    /**
     * 查询附近可用骑手
     */
    Result<List<RiderVO>> findNearbyAvailableRiders(BigDecimal longitude, BigDecimal latitude, BigDecimal radius);
    
    /**
     * 更新骑手订单计数
     */
    Result<String> updateOrderCount(Long riderId, int increment);
}
