package com.gongxiang.zhenxuan.user.service;

import com.gongxiang.zhenxuan.user.api.dto.UserAddressDTO;

import java.util.List;

/**
 * 用户地址服务接口
 *
 * @author gongxiang-zhenxuan
 */
public interface UserAddressService {

    /**
     * 获取用户地址列表
     */
    List<UserAddressDTO> getUserAddressList(Long userId);

    /**
     * 根据地址ID获取地址信息
     */
    UserAddressDTO getAddressById(Long userId, Long addressId);

    /**
     * 新增用户地址
     */
    UserAddressDTO addAddress(Long userId, UserAddressDTO addressDTO);

    /**
     * 更新用户地址
     */
    UserAddressDTO updateAddress(Long userId, UserAddressDTO addressDTO);

    /**
     * 删除用户地址
     */
    void deleteAddress(Long userId, Long addressId);

    /**
     * 设置默认地址
     */
    void setDefaultAddress(Long userId, Long addressId);

    /**
     * 获取用户默认地址
     */
    UserAddressDTO getDefaultAddress(Long userId);

    /**
     * 验证地址是否属于用户
     */
    boolean validateAddressOwner(Long userId, Long addressId);
}