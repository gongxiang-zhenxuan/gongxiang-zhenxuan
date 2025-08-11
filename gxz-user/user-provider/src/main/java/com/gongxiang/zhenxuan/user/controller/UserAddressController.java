package com.gongxiang.zhenxuan.user.controller;

import com.gongxiang.zhenxuan.common.result.Result;
import com.gongxiang.zhenxuan.user.api.dto.UserAddressDTO;
import com.gongxiang.zhenxuan.user.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 用户地址管理控制器
 *
 * @author gongxiang-zhenxuan
 */
@RestController
@RequestMapping("/address")
public class UserAddressController {

    @Autowired
    private UserAddressService userAddressService;

    /**
     * 获取用户地址列表
     */
    @GetMapping("/list")
    public Result<List<UserAddressDTO>> getAddressList(@RequestHeader("X-User-Id") Long userId) {
        List<UserAddressDTO> addressList = userAddressService.getUserAddressList(userId);
        return Result.success(addressList);
    }

    /**
     * 获取地址详情
     */
    @GetMapping("/{addressId}")
    public Result<UserAddressDTO> getAddress(@RequestHeader("X-User-Id") Long userId, 
                                           @PathVariable Long addressId) {
        UserAddressDTO address = userAddressService.getAddressById(userId, addressId);
        return Result.success(address);
    }

    /**
     * 新增用户地址
     */
    @PostMapping
    public Result<UserAddressDTO> addAddress(@RequestHeader("X-User-Id") Long userId, 
                                           @Valid @RequestBody UserAddressDTO addressDTO) {
        UserAddressDTO address = userAddressService.addAddress(userId, addressDTO);
        return Result.success(address);
    }

    /**
     * 更新用户地址
     */
    @PutMapping("/{addressId}")
    public Result<UserAddressDTO> updateAddress(@RequestHeader("X-User-Id") Long userId, 
                                              @PathVariable Long addressId,
                                              @Valid @RequestBody UserAddressDTO addressDTO) {
        addressDTO.setId(addressId);
        UserAddressDTO address = userAddressService.updateAddress(userId, addressDTO);
        return Result.success(address);
    }

    /**
     * 删除用户地址
     */
    @DeleteMapping("/{addressId}")
    public Result<Void> deleteAddress(@RequestHeader("X-User-Id") Long userId, 
                                    @PathVariable Long addressId) {
        userAddressService.deleteAddress(userId, addressId);
        return Result.success();
    }

    /**
     * 设置默认地址
     */
    @PutMapping("/{addressId}/default")
    public Result<Void> setDefaultAddress(@RequestHeader("X-User-Id") Long userId, 
                                        @PathVariable Long addressId) {
        userAddressService.setDefaultAddress(userId, addressId);
        return Result.success();
    }

    /**
     * 获取默认地址
     */
    @GetMapping("/default")
    public Result<UserAddressDTO> getDefaultAddress(@RequestHeader("X-User-Id") Long userId) {
        UserAddressDTO address = userAddressService.getDefaultAddress(userId);
        return Result.success(address);
    }
}