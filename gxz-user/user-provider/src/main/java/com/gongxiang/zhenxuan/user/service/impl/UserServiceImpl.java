package com.gongxiang.zhenxuan.user.service.impl;

import com.gongxiang.zhenxuan.common.exception.BusinessException;
import com.gongxiang.zhenxuan.common.result.ResultCode;
import com.gongxiang.zhenxuan.common.utils.DateUtil;
import com.gongxiang.zhenxuan.common.utils.StringUtil;
import com.gongxiang.zhenxuan.user.api.dto.UserLoginDTO;
import com.gongxiang.zhenxuan.user.api.dto.UserRegisterDTO;
import com.gongxiang.zhenxuan.user.api.vo.LoginVO;
import com.gongxiang.zhenxuan.user.api.vo.UserVO;
import com.gongxiang.zhenxuan.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户服务实现类（模拟实现，无数据库）
 *
 * @author gongxiang-zhenxuan
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final Map<Long, UserVO> userStorage = new ConcurrentHashMap<>();
    private final Map<String, Long> openidUserMap = new ConcurrentHashMap<>();
    private final Map<String, UserVO> tokenUserMap = new ConcurrentHashMap<>();

    @Override
    public UserVO register(UserRegisterDTO registerDTO) {
        log.info("用户注册: {}", registerDTO);

        if (openidUserMap.containsKey(registerDTO.getOpenid())) {
            throw new BusinessException(ResultCode.USER_ALREADY_EXISTS);
        }

        UserVO userVO = new UserVO();
        Long userId = System.currentTimeMillis();
        
        userVO.setId(userId);
        userVO.setOpenid(registerDTO.getOpenid());
        userVO.setPhone(registerDTO.getPhone());
        userVO.setNickname(registerDTO.getNickname());
        userVO.setAvatar(registerDTO.getAvatar());
        userVO.setStatus(1);
        userVO.setCreatedTime(LocalDateTime.now());
        userVO.setUpdatedTime(LocalDateTime.now());

        userStorage.put(userId, userVO);
        openidUserMap.put(registerDTO.getOpenid(), userId);

        log.info("用户注册成功: userId={}, openid={}", userId, registerDTO.getOpenid());
        return userVO;
    }

    @Override
    public LoginVO login(UserLoginDTO loginDTO) {
        log.info("用户登录: {}", loginDTO);

        String mockOpenid = "mock_openid_" + loginDTO.getCode();
        
        UserVO userVO;
        if (openidUserMap.containsKey(mockOpenid)) {
            Long userId = openidUserMap.get(mockOpenid);
            userVO = userStorage.get(userId);
        } else {
            UserRegisterDTO registerDTO = new UserRegisterDTO();
            registerDTO.setOpenid(mockOpenid);
            registerDTO.setNickname("微信用户" + StringUtil.generateRandomNumber(4));
            registerDTO.setPhone(null);
            registerDTO.setAvatar("https://example.com/default-avatar.png");
            
            userVO = register(registerDTO);
        }

        if (userVO.getStatus() != 1) {
            throw new BusinessException(ResultCode.USER_ACCOUNT_DISABLED);
        }

        String token = generateToken(userVO.getId());
        tokenUserMap.put(token, userVO);

        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setExpires(System.currentTimeMillis() + 24 * 60 * 60 * 1000L); // 24小时
        loginVO.setUserInfo(userVO);

        log.info("用户登录成功: userId={}, token={}", userVO.getId(), token);
        return loginVO;
    }

    @Override
    public UserVO getUserById(Long userId) {
        if (userId == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "用户ID不能为空");
        }

        UserVO userVO = userStorage.get(userId);
        if (userVO == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        return userVO;
    }

    @Override
    public UserVO getUserByOpenid(String openid) {
        if (StringUtil.isBlank(openid)) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "openid不能为空");
        }

        Long userId = openidUserMap.get(openid);
        if (userId == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        return getUserById(userId);
    }

    @Override
    public UserVO updateUser(Long userId, UserRegisterDTO updateDTO) {
        log.info("更新用户信息: userId={}, updateDTO={}", userId, updateDTO);

        UserVO userVO = getUserById(userId);

        if (StringUtil.isNotBlank(updateDTO.getNickname())) {
            userVO.setNickname(updateDTO.getNickname());
        }
        if (StringUtil.isNotBlank(updateDTO.getAvatar())) {
            userVO.setAvatar(updateDTO.getAvatar());
        }
        if (StringUtil.isNotBlank(updateDTO.getPhone())) {
            userVO.setPhone(updateDTO.getPhone());
        }
        
        userVO.setUpdatedTime(LocalDateTime.now());
        userStorage.put(userId, userVO);

        log.info("用户信息更新成功: userId={}", userId);
        return userVO;
    }

    @Override
    public void logout(Long userId) {
        log.info("用户退出登录: userId={}", userId);
        
        tokenUserMap.entrySet().removeIf(entry -> 
            entry.getValue().getId().equals(userId));

        log.info("用户退出登录成功: userId={}", userId);
    }

    @Override
    public UserVO validateToken(String token) {
        if (StringUtil.isBlank(token)) {
            throw new BusinessException(ResultCode.USER_TOKEN_INVALID);
        }

        UserVO userVO = tokenUserMap.get(token);
        if (userVO == null) {
            throw new BusinessException(ResultCode.USER_TOKEN_EXPIRED);
        }

        return userVO;
    }

    @Override
    public String refreshToken(String token) {
        UserVO userVO = validateToken(token);
        
        tokenUserMap.remove(token);
        
        String newToken = generateToken(userVO.getId());
        tokenUserMap.put(newToken, userVO);
        
        return newToken;
    }

    private String generateToken(Long userId) {
        return "gxz_token_" + userId + "_" + System.currentTimeMillis() + "_" + StringUtil.generateRandomString(8);
    }
}