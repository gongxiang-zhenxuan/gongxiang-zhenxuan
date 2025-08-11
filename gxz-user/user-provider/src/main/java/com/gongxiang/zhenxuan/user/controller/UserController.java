package com.gongxiang.zhenxuan.user.controller;

import com.gongxiang.zhenxuan.common.result.Result;
import com.gongxiang.zhenxuan.user.api.dto.UserLoginDTO;
import com.gongxiang.zhenxuan.user.api.dto.UserRegisterDTO;
import com.gongxiang.zhenxuan.user.api.vo.LoginVO;
import com.gongxiang.zhenxuan.user.api.vo.UserVO;
import com.gongxiang.zhenxuan.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用户管理控制器
 *
 * @author gongxiang-zhenxuan
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<UserVO> register(@Valid @RequestBody UserRegisterDTO registerDTO) {
        UserVO userVO = userService.register(registerDTO);
        return Result.success(userVO);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        LoginVO loginVO = userService.login(loginDTO);
        return Result.success(loginVO);
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/profile")
    public Result<UserVO> profile(@RequestHeader("X-User-Id") Long userId) {
        UserVO userVO = userService.getUserById(userId);
        return Result.success(userVO);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/profile")
    public Result<UserVO> updateProfile(@RequestHeader("X-User-Id") Long userId, 
                                      @Valid @RequestBody UserRegisterDTO updateDTO) {
        UserVO userVO = userService.updateUser(userId, updateDTO);
        return Result.success(userVO);
    }

    /**
     * 用户退出登录
     */
    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader("X-User-Id") Long userId) {
        userService.logout(userId);
        return Result.success();
    }
}