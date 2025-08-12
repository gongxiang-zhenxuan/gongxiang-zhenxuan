package com.gongxiang.zhenxuan.delivery.controller;

import com.gongxiang.zhenxuan.common.result.Result;
import com.gongxiang.zhenxuan.delivery.api.dto.*;
import com.gongxiang.zhenxuan.delivery.api.vo.DeliveryOrderVO;
import com.gongxiang.zhenxuan.delivery.api.vo.RiderVO;
import com.gongxiang.zhenxuan.delivery.service.OrderGrabService;
import com.gongxiang.zhenxuan.delivery.service.RiderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

/**
 * 骑手管理控制器
 */
@Slf4j
@Api(tags = "骑手管理")
@RestController
@RequestMapping("/api/rider")
@Validated
public class RiderController {

    @Autowired
    private RiderService riderService;
    
    @Autowired
    private OrderGrabService orderGrabService;

    @ApiOperation("骑手注册")
    @PostMapping("/register")
    public Result<String> register(@Valid @RequestBody RiderRegisterDTO dto) {
        log.info("骑手注册请求：{}", dto.getPhone());
        return riderService.register(dto);
    }

    @ApiOperation("获取骑手信息")
    @GetMapping("/info/{riderId}")
    public Result<RiderVO> getRiderInfo(@PathVariable Long riderId) {
        log.info("获取骑手信息，riderId：{}", riderId);
        return riderService.getRiderInfo(riderId);
    }

    @ApiOperation("更新在线状态")
    @PostMapping("/status/update")
    public Result<String> updateOnlineStatus(@Valid @RequestBody RiderStatusUpdateDTO dto) {
        log.info("更新骑手状态，riderId：{}，状态：{}", dto.getRiderId(), dto.getOnlineStatus());
        return riderService.updateOnlineStatus(dto);
    }

    @ApiOperation("更新位置信息")
    @PostMapping("/location/update")
    public Result<String> updateLocation(@Valid @RequestBody RiderLocationUpdateDTO dto) {
        return riderService.updateLocation(dto);
    }

    @ApiOperation("查询附近可抢订单")
    @PostMapping("/orders/nearby")
    public Result<List<DeliveryOrderVO>> findNearbyOrders(@Valid @RequestBody NearbyOrderQueryDTO dto) {
        log.info("查询附近订单，骑手位置：{}，{}", dto.getLongitude(), dto.getLatitude());
        
        BigDecimal serviceRange = dto.getServiceRange() != null ? 
            dto.getServiceRange() : new BigDecimal("5.0");
            
        return orderGrabService.findNearbyGrabbableOrders(
            dto.getLongitude(), dto.getLatitude(), serviceRange);
    }

    @ApiOperation("抢单")
    @PostMapping("/order/grab")
    public Result<String> grabOrder(@Valid @RequestBody OrderGrabDTO dto) {
        log.info("骑手抢单，riderId：{}，orderId：{}", dto.getRiderId(), dto.getOrderId());
        return orderGrabService.grabOrder(dto);
    }
}