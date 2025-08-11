package com.gongxiang.zhenxuan.merchant.service;

import com.gongxiang.zhenxuan.merchant.api.dto.MerchantRegisterDTO;
import com.gongxiang.zhenxuan.merchant.api.dto.MerchantStatusDTO;
import com.gongxiang.zhenxuan.merchant.api.vo.MerchantVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商户服务接口
 *
 * @author gongxiang-zhenxuan
 */
public interface MerchantService {

    /**
     * 商户注册/入驻申请
     */
    MerchantVO register(MerchantRegisterDTO registerDTO);

    /**
     * 根据商户ID获取商户信息
     */
    MerchantVO getMerchantById(Long merchantId);

    /**
     * 更新商户信息
     */
    MerchantVO updateMerchant(Long merchantId, MerchantRegisterDTO updateDTO);

    /**
     * 更新商户状态
     */
    MerchantVO updateStatus(Long merchantId, MerchantStatusDTO statusDTO);

    /**
     * 获取附近商户列表
     */
    List<MerchantVO> getNearbyMerchants(BigDecimal longitude, BigDecimal latitude, Double radius);

    /**
     * 搜索商户
     */
    List<MerchantVO> searchMerchants(String keyword, BigDecimal longitude, BigDecimal latitude, Double radius);

    /**
     * 获取商户列表（分页）
     */
    List<MerchantVO> getMerchantList(Integer page, Integer size, Integer status);

    /**
     * 审核商户（管理后台）
     */
    MerchantVO auditMerchant(Long merchantId, Integer status, String reason);

    /**
     * 验证商户是否可以营业
     */
    boolean isMerchantAvailable(Long merchantId);

    /**
     * 计算距离（公里）
     */
    Double calculateDistance(BigDecimal lng1, BigDecimal lat1, BigDecimal lng2, BigDecimal lat2);
}