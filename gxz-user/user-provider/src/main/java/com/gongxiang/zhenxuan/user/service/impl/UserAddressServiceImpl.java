package com.gongxiang.zhenxuan.user.service.impl;

import com.gongxiang.zhenxuan.common.exception.BusinessException;
import com.gongxiang.zhenxuan.common.result.ResultCode;
import com.gongxiang.zhenxuan.user.api.dto.UserAddressDTO;
import com.gongxiang.zhenxuan.user.service.UserAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 用户地址服务实现类（模拟实现，无数据库）
 *
 * @author gongxiang-zhenxuan
 */
@Slf4j
@Service
public class UserAddressServiceImpl implements UserAddressService {

    private final Map<Long, UserAddressDTO> addressStorage = new ConcurrentHashMap<>();
    private final Map<Long, List<Long>> userAddressMap = new ConcurrentHashMap<>();

    @Override
    public List<UserAddressDTO> getUserAddressList(Long userId) {
        log.info("获取用户地址列表: userId={}", userId);

        List<Long> addressIds = userAddressMap.getOrDefault(userId, new ArrayList<>());
        List<UserAddressDTO> addresses = addressIds.stream()
                .map(addressStorage::get)
                .collect(Collectors.toList());

        log.info("获取用户地址列表成功: userId={}, count={}", userId, addresses.size());
        return addresses;
    }

    @Override
    public UserAddressDTO getAddressById(Long userId, Long addressId) {
        log.info("获取地址详情: userId={}, addressId={}", userId, addressId);

        if (!validateAddressOwner(userId, addressId)) {
            throw new BusinessException(ResultCode.FORBIDDEN, "无权限访问该地址");
        }

        UserAddressDTO address = addressStorage.get(addressId);
        if (address == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "地址不存在");
        }

        return address;
    }

    @Override
    public UserAddressDTO addAddress(Long userId, UserAddressDTO addressDTO) {
        log.info("新增用户地址: userId={}, address={}", userId, addressDTO);

        Long addressId = System.currentTimeMillis();
        addressDTO.setId(addressId);

        if (addressDTO.getIsDefault() != null && addressDTO.getIsDefault()) {
            clearDefaultAddress(userId);
        }

        addressStorage.put(addressId, addressDTO);
        
        List<Long> userAddresses = userAddressMap.computeIfAbsent(userId, k -> new ArrayList<>());
        userAddresses.add(addressId);

        log.info("用户地址新增成功: userId={}, addressId={}", userId, addressId);
        return addressDTO;
    }

    @Override
    public UserAddressDTO updateAddress(Long userId, UserAddressDTO addressDTO) {
        log.info("更新用户地址: userId={}, address={}", userId, addressDTO);

        if (!validateAddressOwner(userId, addressDTO.getId())) {
            throw new BusinessException(ResultCode.FORBIDDEN, "无权限修改该地址");
        }

        UserAddressDTO existingAddress = addressStorage.get(addressDTO.getId());
        if (existingAddress == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "地址不存在");
        }

        if (addressDTO.getIsDefault() != null && addressDTO.getIsDefault()) {
            clearDefaultAddress(userId);
        }

        existingAddress.setName(addressDTO.getName());
        existingAddress.setPhone(addressDTO.getPhone());
        existingAddress.setProvince(addressDTO.getProvince());
        existingAddress.setCity(addressDTO.getCity());
        existingAddress.setArea(addressDTO.getArea());
        existingAddress.setStreet(addressDTO.getStreet());
        existingAddress.setLongitude(addressDTO.getLongitude());
        existingAddress.setLatitude(addressDTO.getLatitude());
        existingAddress.setIsDefault(addressDTO.getIsDefault());

        addressStorage.put(addressDTO.getId(), existingAddress);

        log.info("用户地址更新成功: userId={}, addressId={}", userId, addressDTO.getId());
        return existingAddress;
    }

    @Override
    public void deleteAddress(Long userId, Long addressId) {
        log.info("删除用户地址: userId={}, addressId={}", userId, addressId);

        if (!validateAddressOwner(userId, addressId)) {
            throw new BusinessException(ResultCode.FORBIDDEN, "无权限删除该地址");
        }

        addressStorage.remove(addressId);
        
        List<Long> userAddresses = userAddressMap.get(userId);
        if (userAddresses != null) {
            userAddresses.remove(addressId);
        }

        log.info("用户地址删除成功: userId={}, addressId={}", userId, addressId);
    }

    @Override
    public void setDefaultAddress(Long userId, Long addressId) {
        log.info("设置默认地址: userId={}, addressId={}", userId, addressId);

        if (!validateAddressOwner(userId, addressId)) {
            throw new BusinessException(ResultCode.FORBIDDEN, "无权限设置该地址为默认");
        }

        clearDefaultAddress(userId);

        UserAddressDTO address = addressStorage.get(addressId);
        if (address != null) {
            address.setIsDefault(true);
            addressStorage.put(addressId, address);
        }

        log.info("默认地址设置成功: userId={}, addressId={}", userId, addressId);
    }

    @Override
    public UserAddressDTO getDefaultAddress(Long userId) {
        log.info("获取默认地址: userId={}", userId);

        List<UserAddressDTO> addresses = getUserAddressList(userId);
        UserAddressDTO defaultAddress = addresses.stream()
                .filter(address -> address.getIsDefault() != null && address.getIsDefault())
                .findFirst()
                .orElse(null);

        log.info("获取默认地址: userId={}, found={}", userId, defaultAddress != null);
        return defaultAddress;
    }

    @Override
    public boolean validateAddressOwner(Long userId, Long addressId) {
        List<Long> userAddresses = userAddressMap.get(userId);
        return userAddresses != null && userAddresses.contains(addressId);
    }

    private void clearDefaultAddress(Long userId) {
        List<UserAddressDTO> addresses = getUserAddressList(userId);
        addresses.stream()
                .filter(address -> address.getIsDefault() != null && address.getIsDefault())
                .forEach(address -> {
                    address.setIsDefault(false);
                    addressStorage.put(address.getId(), address);
                });
    }
}