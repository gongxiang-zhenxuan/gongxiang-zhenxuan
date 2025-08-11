package com.gongxiang.zhenxuan.merchant.controller;

import com.gongxiang.zhenxuan.common.result.Result;
import com.gongxiang.zhenxuan.merchant.api.dto.MerchantRegisterDTO;
import com.gongxiang.zhenxuan.merchant.api.dto.MerchantStatusDTO;
import com.gongxiang.zhenxuan.merchant.api.vo.MerchantVO;
import com.gongxiang.zhenxuan.merchant.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

/**
 * 商户管理控制器
 *
 * @author gongxiang-zhenxuan
 */
@RestController
@RequestMapping("/merchant")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    /**
     * 商户注册/入驻申请
     */
    @PostMapping("/register")
    public Result<MerchantVO> register(@Valid @RequestBody MerchantRegisterDTO registerDTO) {
        MerchantVO merchantVO = merchantService.register(registerDTO);
        return Result.success(merchantVO);
    }

    /**
     * 获取商户信息
     */
    @GetMapping("/{merchantId}")
    public Result<MerchantVO> getMerchant(@PathVariable Long merchantId) {
        MerchantVO merchantVO = merchantService.getMerchantById(merchantId);
        return Result.success(merchantVO);
    }

    /**
     * 获取商户信息（商户端调用）
     */
    @GetMapping("/info")
    public Result<MerchantVO> getMerchantInfo(@RequestHeader("X-User-Id") Long merchantId) {
        MerchantVO merchantVO = merchantService.getMerchantById(merchantId);
        return Result.success(merchantVO);
    }

    /**
     * 更新商户信息
     */
    @PutMapping("/info")
    public Result<MerchantVO> updateMerchant(@RequestHeader("X-User-Id") Long merchantId,
                                           @Valid @RequestBody MerchantRegisterDTO updateDTO) {
        MerchantVO merchantVO = merchantService.updateMerchant(merchantId, updateDTO);
        return Result.success(merchantVO);
    }

    /**
     * 更新商户营业状态
     */
    @PutMapping("/status")
    public Result<MerchantVO> updateStatus(@RequestHeader("X-User-Id") Long merchantId,
                                         @Valid @RequestBody MerchantStatusDTO statusDTO) {
        MerchantVO merchantVO = merchantService.updateStatus(merchantId, statusDTO);
        return Result.success(merchantVO);
    }

    /**
     * 获取附近商户列表
     */
    @GetMapping("/nearby")
    public Result<List<MerchantVO>> getNearbyMerchants(@RequestParam BigDecimal longitude,
                                                     @RequestParam BigDecimal latitude,
                                                     @RequestParam(defaultValue = "5.0") Double radius) {
        List<MerchantVO> merchants = merchantService.getNearbyMerchants(longitude, latitude, radius);
        return Result.success(merchants);
    }

    /**
     * 搜索商户
     */
    @GetMapping("/search")
    public Result<List<MerchantVO>> searchMerchants(@RequestParam String keyword,
                                                  @RequestParam(required = false) BigDecimal longitude,
                                                  @RequestParam(required = false) BigDecimal latitude,
                                                  @RequestParam(defaultValue = "10.0") Double radius) {
        List<MerchantVO> merchants = merchantService.searchMerchants(keyword, longitude, latitude, radius);
        return Result.success(merchants);
    }

    /**
     * 获取商户列表（管理后台）
     */
    @GetMapping("/list")
    public Result<List<MerchantVO>> getMerchantList(@RequestParam(defaultValue = "1") Integer page,
                                                  @RequestParam(defaultValue = "10") Integer size,
                                                  @RequestParam(required = false) Integer status) {
        List<MerchantVO> merchants = merchantService.getMerchantList(page, size, status);
        return Result.success(merchants);
    }
}