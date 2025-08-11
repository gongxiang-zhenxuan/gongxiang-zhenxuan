package com.gongxiang.zhenxuan.user.service;

import com.gongxiang.zhenxuan.user.api.dto.UserLoginDTO;
import com.gongxiang.zhenxuan.user.api.dto.UserRegisterDTO;
import com.gongxiang.zhenxuan.user.api.vo.LoginVO;
import com.gongxiang.zhenxuan.user.api.vo.UserVO;

/**
 * 用户服务接口
 *
 * @author gongxiang-zhenxuan
 */
public interface UserService {

    /**
     * 用户注册
     */
    UserVO register(UserRegisterDTO registerDTO);

    /**
     * 用户登录
     */
    LoginVO login(UserLoginDTO loginDTO);

    /**
     * 根据用户ID获取用户信息
     */
    UserVO getUserById(Long userId);

    /**
     * 根据openid获取用户信息
     */
    UserVO getUserByOpenid(String openid);

    /**
     * 更新用户信息
     */
    UserVO updateUser(Long userId, UserRegisterDTO updateDTO);

    /**
     * 用户退出登录
     */
    void logout(Long userId);

    /**
     * 验证用户token
     */
    UserVO validateToken(String token);

    /**
     * 刷新用户token
     */
    String refreshToken(String token);
}