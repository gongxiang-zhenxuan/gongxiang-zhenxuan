package com.gongxiang.zhenxuan.delivery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gongxiang.zhenxuan.common.result.Result;
import com.gongxiang.zhenxuan.common.result.ResultCode;
import com.gongxiang.zhenxuan.common.utils.StringUtil;
import com.gongxiang.zhenxuan.delivery.api.dto.RiderLocationUpdateDTO;
import com.gongxiang.zhenxuan.delivery.api.dto.RiderRegisterDTO;
import com.gongxiang.zhenxuan.delivery.api.dto.RiderStatusUpdateDTO;
import com.gongxiang.zhenxuan.delivery.api.enums.RiderStatus;
import com.gongxiang.zhenxuan.delivery.api.vo.RiderVO;
import com.gongxiang.zhenxuan.delivery.entity.Rider;
import com.gongxiang.zhenxuan.delivery.mapper.RiderMapper;
import com.gongxiang.zhenxuan.delivery.service.RiderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 骑手管理服务实现
 */
@Slf4j
@Service
public class RiderServiceImpl extends ServiceImpl<RiderMapper, Rider> implements RiderService {

    @Autowired
    private RiderMapper riderMapper;
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    private static final String RIDER_LOCATION_KEY = "rider:location:";
    private static final String RIDER_CODE_PREFIX = "R";
    
    @Override
    public Result<String> register(RiderRegisterDTO dto) {
        try {
            // 检查手机号是否已存在
            LambdaQueryWrapper<Rider> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Rider::getPhone, dto.getPhone());
            if (count(queryWrapper) > 0) {
                return Result.fail(ResultCode.RIDER_PHONE_EXISTS);
            }
            
            // 检查身份证是否已存在
            queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Rider::getIdCard, dto.getIdCard());
            if (count(queryWrapper) > 0) {
                return Result.fail(ResultCode.RIDER_IDCARD_EXISTS);
            }
            
            // 创建骑手记录
            Rider rider = new Rider();
            BeanUtils.copyProperties(dto, rider);
            rider.setRiderCode(generateRiderCode());
            rider.setStatus(RiderStatus.NORMAL.getCode());
            rider.setOnlineStatus(3); // 离线
            rider.setCurrentOrdersCount(0);
            rider.setMaxConcurrentOrders(3); // 默认最大3单
            rider.setServiceRange(new BigDecimal("5.0")); // 默认5公里
            rider.setRating(new BigDecimal("5.0")); // 默认5分
            rider.setTotalOrders(0);
            rider.setCompletedOrders(0);
            rider.setRegisterTime(LocalDateTime.now());
            
            save(rider);
            
            log.info("骑手注册成功，骑手编号：{}，手机号：{}", rider.getRiderCode(), rider.getPhone());
            return Result.success("注册成功");
            
        } catch (Exception e) {
            log.error("骑手注册失败", e);
            return Result.fail("注册失败");
        }
    }
    
    @Override
    public Result<RiderVO> getRiderInfo(Long riderId) {
        try {
            Rider rider = getById(riderId);
            if (rider == null) {
                return Result.fail(ResultCode.RIDER_NOT_FOUND);
            }
            
            RiderVO vo = new RiderVO();
            BeanUtils.copyProperties(rider, vo);
            
            return Result.success(vo);
            
        } catch (Exception e) {
            log.error("获取骑手信息失败，riderId：{}", riderId, e);
            return Result.fail("获取骑手信息失败");
        }
    }
    
    @Override
    public Result<String> updateOnlineStatus(RiderStatusUpdateDTO dto) {
        try {
            Rider rider = getById(dto.getRiderId());
            if (rider == null) {
                return Result.fail(ResultCode.RIDER_NOT_FOUND);
            }
            
            if (rider.getStatus().equals(RiderStatus.FROZEN.getCode()) ||
                rider.getStatus().equals(RiderStatus.DISABLED.getCode())) {
                return Result.fail("账户状态异常，无法上线");
            }
            
            LambdaUpdateWrapper<Rider> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Rider::getId, dto.getRiderId())
                        .set(Rider::getOnlineStatus, dto.getOnlineStatus());
            
            if (dto.getOnlineStatus() == 1) { // 上线
                updateWrapper.set(Rider::getLastOnlineTime, LocalDateTime.now());
            }
            
            update(updateWrapper);
            
            // 更新Redis中的骑手位置信息
            if (dto.getLongitude() != null && dto.getLatitude() != null) {
                updateRiderLocation(dto.getRiderId(), dto.getLongitude(), dto.getLatitude());
            }
            
            log.info("骑手状态更新成功，riderId：{}，状态：{}", dto.getRiderId(), dto.getOnlineStatus());
            return Result.success("状态更新成功");
            
        } catch (Exception e) {
            log.error("更新骑手状态失败", e);
            return Result.fail("状态更新失败");
        }
    }
    
    @Override
    public Result<String> updateLocation(RiderLocationUpdateDTO dto) {
        try {
            // 更新数据库中的位置信息
            LambdaUpdateWrapper<Rider> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Rider::getId, dto.getRiderId())
                        .set(Rider::getCurrentLongitude, dto.getLongitude())
                        .set(Rider::getCurrentLatitude, dto.getLatitude());
            
            update(updateWrapper);
            
            // 更新Redis中的实时位置
            updateRiderLocation(dto.getRiderId(), dto.getLongitude(), dto.getLatitude());
            
            return Result.success("位置更新成功");
            
        } catch (Exception e) {
            log.error("更新骑手位置失败", e);
            return Result.fail("位置更新失败");
        }
    }
    
    @Override
    public Result<List<RiderVO>> findNearbyAvailableRiders(BigDecimal longitude, BigDecimal latitude, 
                                                          BigDecimal radius) {
        try {
            // 计算经纬度范围
            BigDecimal lngDelta = radius.divide(new BigDecimal("111.320"), 6, BigDecimal.ROUND_HALF_UP);
            BigDecimal latDelta = radius.divide(new BigDecimal("110.540"), 6, BigDecimal.ROUND_HALF_UP);
            
            BigDecimal minLng = longitude.subtract(lngDelta);
            BigDecimal maxLng = longitude.add(lngDelta);
            BigDecimal minLat = latitude.subtract(latDelta);
            BigDecimal maxLat = latitude.add(latDelta);
            
            // 查询附近在线骑手
            LambdaQueryWrapper<Rider> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Rider::getStatus, RiderStatus.NORMAL.getCode())
                        .eq(Rider::getOnlineStatus, 1) // 在线
                        .le(Rider::getCurrentOrdersCount, 2) // 当前订单数少于最大值
                        .between(Rider::getCurrentLongitude, minLng, maxLng)
                        .between(Rider::getCurrentLatitude, minLat, maxLat)
                        .orderByDesc(Rider::getRating)
                        .orderByAsc(Rider::getCurrentOrdersCount)
                        .last("LIMIT 20");
            
            List<Rider> riders = list(queryWrapper);
            
            List<RiderVO> result = riders.stream()
                    .map(rider -> {
                        RiderVO vo = new RiderVO();
                        BeanUtils.copyProperties(rider, vo);
                        return vo;
                    })
                    .collect(Collectors.toList());
            
            return Result.success(result);
            
        } catch (Exception e) {
            log.error("查询附近骑手失败", e);
            return Result.fail("查询附近骑手失败");
        }
    }
    
    @Override
    public Result<String> updateOrderCount(Long riderId, int increment) {
        try {
            LambdaUpdateWrapper<Rider> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Rider::getId, riderId);
            
            if (increment > 0) {
                updateWrapper.setSql("current_orders_count = current_orders_count + " + increment);
                updateWrapper.setSql("total_orders = total_orders + " + increment);
            } else {
                updateWrapper.setSql("current_orders_count = current_orders_count - " + Math.abs(increment));
                updateWrapper.setSql("completed_orders = completed_orders + " + Math.abs(increment));
            }
            
            update(updateWrapper);
            
            log.info("更新骑手订单计数，riderId：{}，increment：{}", riderId, increment);
            return Result.success("订单计数更新成功");
            
        } catch (Exception e) {
            log.error("更新骑手订单计数失败", e);
            return Result.fail("订单计数更新失败");
        }
    }
    
    /**
     * 生成骑手编号
     */
    private String generateRiderCode() {
        long timestamp = System.currentTimeMillis();
        return RIDER_CODE_PREFIX + timestamp;
    }
    
    /**
     * 更新Redis中的骑手位置
     */
    private void updateRiderLocation(Long riderId, BigDecimal longitude, BigDecimal latitude) {
        try {
            String locationKey = RIDER_LOCATION_KEY + riderId;
            String locationValue = longitude + "," + latitude + "," + System.currentTimeMillis();
            
            redisTemplate.opsForValue().set(locationKey, locationValue, 5, TimeUnit.MINUTES);
            
            // 添加到地理位置索引
            redisTemplate.opsForGeo().add("rider:geo", 
                new org.springframework.data.geo.Point(longitude.doubleValue(), latitude.doubleValue()), 
                riderId.toString());
                
        } catch (Exception e) {
            log.error("更新Redis位置信息失败", e);
        }
    }
}
