package com.gongxiang.zhenxuan.merchant.service.impl;

import com.gongxiang.zhenxuan.common.exception.BusinessException;
import com.gongxiang.zhenxuan.common.result.ResultCode;
import com.gongxiang.zhenxuan.common.utils.StringUtil;
import com.gongxiang.zhenxuan.merchant.api.dto.MerchantRegisterDTO;
import com.gongxiang.zhenxuan.merchant.api.dto.MerchantStatusDTO;
import com.gongxiang.zhenxuan.merchant.api.enums.MerchantStatus;
import com.gongxiang.zhenxuan.merchant.api.vo.MerchantVO;
import com.gongxiang.zhenxuan.merchant.service.MerchantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 商户服务实现类（模拟实现，无数据库）
 *
 * @author gongxiang-zhenxuan
 */
@Slf4j
@Service
public class MerchantServiceImpl implements MerchantService {

    private final Map<Long, MerchantVO> merchantStorage = new ConcurrentHashMap<>();

    @Override
    public MerchantVO register(MerchantRegisterDTO registerDTO) {
        log.info("商户注册: {}", registerDTO);

        MerchantVO merchantVO = new MerchantVO();
        Long merchantId = System.currentTimeMillis();

        merchantVO.setId(merchantId);
        merchantVO.setName(registerDTO.getName());
        merchantVO.setPhone(registerDTO.getPhone());
        merchantVO.setAddress(registerDTO.getAddress());
        merchantVO.setLongitude(registerDTO.getLongitude());
        merchantVO.setLatitude(registerDTO.getLatitude());
        merchantVO.setDescription(registerDTO.getDescription());
        merchantVO.setBusinessLicense(registerDTO.getBusinessLicense());
        merchantVO.setContactPerson(registerDTO.getContactPerson());
        merchantVO.setEmail(registerDTO.getEmail());
        merchantVO.setStatus(MerchantStatus.PENDING.getCode());
        merchantVO.setStatusDesc(MerchantStatus.PENDING.getDescription());
        merchantVO.setCreatedTime(LocalDateTime.now());
        merchantVO.setUpdatedTime(LocalDateTime.now());

        merchantStorage.put(merchantId, merchantVO);

        log.info("商户注册成功: merchantId={}, name={}", merchantId, registerDTO.getName());
        return merchantVO;
    }

    @Override
    public MerchantVO getMerchantById(Long merchantId) {
        if (merchantId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "商户ID不能为空");
        }

        MerchantVO merchantVO = merchantStorage.get(merchantId);
        if (merchantVO == null) {
            throw new BusinessException(ResultCode.MERCHANT_NOT_FOUND);
        }

        return merchantVO;
    }

    @Override
    public MerchantVO updateMerchant(Long merchantId, MerchantRegisterDTO updateDTO) {
        log.info("更新商户信息: merchantId={}, updateDTO={}", merchantId, updateDTO);

        MerchantVO merchantVO = getMerchantById(merchantId);

        if (StringUtil.isNotBlank(updateDTO.getName())) {
            merchantVO.setName(updateDTO.getName());
        }
        if (StringUtil.isNotBlank(updateDTO.getPhone())) {
            merchantVO.setPhone(updateDTO.getPhone());
        }
        if (StringUtil.isNotBlank(updateDTO.getAddress())) {
            merchantVO.setAddress(updateDTO.getAddress());
        }
        if (updateDTO.getLongitude() != null) {
            merchantVO.setLongitude(updateDTO.getLongitude());
        }
        if (updateDTO.getLatitude() != null) {
            merchantVO.setLatitude(updateDTO.getLatitude());
        }
        if (StringUtil.isNotBlank(updateDTO.getDescription())) {
            merchantVO.setDescription(updateDTO.getDescription());
        }
        if (StringUtil.isNotBlank(updateDTO.getContactPerson())) {
            merchantVO.setContactPerson(updateDTO.getContactPerson());
        }
        if (StringUtil.isNotBlank(updateDTO.getEmail())) {
            merchantVO.setEmail(updateDTO.getEmail());
        }

        merchantVO.setUpdatedTime(LocalDateTime.now());
        merchantStorage.put(merchantId, merchantVO);

        log.info("商户信息更新成功: merchantId={}", merchantId);
        return merchantVO;
    }

    @Override
    public MerchantVO updateStatus(Long merchantId, MerchantStatusDTO statusDTO) {
        log.info("更新商户状态: merchantId={}, statusDTO={}", merchantId, statusDTO);

        MerchantVO merchantVO = getMerchantById(merchantId);

        MerchantStatus status = MerchantStatus.getByCode(statusDTO.getStatus());
        if (status == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "无效的状态码");
        }

        merchantVO.setStatus(status.getCode());
        merchantVO.setStatusDesc(status.getDescription());
        merchantVO.setUpdatedTime(LocalDateTime.now());

        merchantStorage.put(merchantId, merchantVO);

        log.info("商户状态更新成功: merchantId={}, status={}", merchantId, status.getDescription());
        return merchantVO;
    }

    @Override
    public List<MerchantVO> getNearbyMerchants(BigDecimal longitude, BigDecimal latitude, Double radius) {
        log.info("获取附近商户: longitude={}, latitude={}, radius={}km", longitude, latitude, radius);

        List<MerchantVO> nearbyMerchants = merchantStorage.values().stream()
                .filter(merchant -> MerchantStatus.APPROVED.getCode().equals(merchant.getStatus()))
                .filter(merchant -> {
                    Double distance = calculateDistance(longitude, latitude, 
                                                      merchant.getLongitude(), merchant.getLatitude());
                    return distance <= radius;
                })
                .collect(Collectors.toList());

        log.info("附近商户查询完成: 找到{}家商户", nearbyMerchants.size());
        return nearbyMerchants;
    }

    @Override
    public List<MerchantVO> searchMerchants(String keyword, BigDecimal longitude, BigDecimal latitude, Double radius) {
        log.info("搜索商户: keyword={}, longitude={}, latitude={}, radius={}km", keyword, longitude, latitude, radius);

        List<MerchantVO> merchants = merchantStorage.values().stream()
                .filter(merchant -> MerchantStatus.APPROVED.getCode().equals(merchant.getStatus()))
                .filter(merchant -> {
                    if (StringUtil.isBlank(keyword)) {
                        return true;
                    }
                    return merchant.getName().contains(keyword) || 
                           (merchant.getDescription() != null && merchant.getDescription().contains(keyword));
                })
                .collect(Collectors.toList());

        if (longitude != null && latitude != null && radius != null) {
            merchants = merchants.stream()
                    .filter(merchant -> {
                        Double distance = calculateDistance(longitude, latitude,
                                                          merchant.getLongitude(), merchant.getLatitude());
                        return distance <= radius;
                    })
                    .collect(Collectors.toList());
        }

        log.info("商户搜索完成: 找到{}家商户", merchants.size());
        return merchants;
    }

    @Override
    public List<MerchantVO> getMerchantList(Integer page, Integer size, Integer status) {
        log.info("获取商户列表: page={}, size={}, status={}", page, size, status);

        List<MerchantVO> merchants = merchantStorage.values().stream()
                .filter(merchant -> status == null || status.equals(merchant.getStatus()))
                .skip((long) (page - 1) * size)
                .limit(size)
                .collect(Collectors.toList());

        log.info("商户列表查询完成: 返回{}条记录", merchants.size());
        return merchants;
    }

    @Override
    public MerchantVO auditMerchant(Long merchantId, Integer status, String reason) {
        log.info("审核商户: merchantId={}, status={}, reason={}", merchantId, status, reason);

        MerchantVO merchantVO = getMerchantById(merchantId);

        if (!MerchantStatus.PENDING.getCode().equals(merchantVO.getStatus())) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "只能审核待审核状态的商户");
        }

        MerchantStatus newStatus = MerchantStatus.getByCode(status);
        if (newStatus == null || (!MerchantStatus.APPROVED.equals(newStatus) && !MerchantStatus.REJECTED.equals(newStatus))) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "审核状态只能是通过或拒绝");
        }

        merchantVO.setStatus(newStatus.getCode());
        merchantVO.setStatusDesc(newStatus.getDescription());
        merchantVO.setUpdatedTime(LocalDateTime.now());

        merchantStorage.put(merchantId, merchantVO);

        log.info("商户审核完成: merchantId={}, result={}", merchantId, newStatus.getDescription());
        return merchantVO;
    }

    @Override
    public boolean isMerchantAvailable(Long merchantId) {
        try {
            MerchantVO merchantVO = getMerchantById(merchantId);
            return MerchantStatus.APPROVED.getCode().equals(merchantVO.getStatus());
        } catch (BusinessException e) {
            return false;
        }
    }

    @Override
    public Double calculateDistance(BigDecimal lng1, BigDecimal lat1, BigDecimal lng2, BigDecimal lat2) {
        if (lng1 == null || lat1 == null || lng2 == null || lat2 == null) {
            return Double.MAX_VALUE;
        }

        double radLat1 = Math.toRadians(lat1.doubleValue());
        double radLat2 = Math.toRadians(lat2.doubleValue());
        double deltaLat = radLat1 - radLat2;
        double deltaLng = Math.toRadians(lng1.doubleValue()) - Math.toRadians(lng2.doubleValue());

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                   Math.cos(radLat1) * Math.cos(radLat2) *
                   Math.sin(deltaLng / 2) * Math.sin(deltaLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double earthRadius = 6371.0; // 地球半径，单位：公里
        double distance = earthRadius * c;

        return BigDecimal.valueOf(distance).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}