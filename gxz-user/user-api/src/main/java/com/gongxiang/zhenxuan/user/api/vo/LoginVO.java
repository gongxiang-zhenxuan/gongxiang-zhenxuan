package com.gongxiang.zhenxuan.user.api.vo;

/**
 * 登录响应VO
 *
 * @author gongxiang-zhenxuan
 */
public class LoginVO {

    private String token;

    private Long expires;

    private UserVO userInfo;

    public LoginVO() {
    }

    public LoginVO(String token, Long expires, UserVO userInfo) {
        this.token = token;
        this.expires = expires;
        this.userInfo = userInfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getExpires() {
        return expires;
    }

    public void setExpires(Long expires) {
        this.expires = expires;
    }

    public UserVO getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserVO userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public String toString() {
        return "LoginVO{" +
                "token='" + token + '\'' +
                ", expires=" + expires +
                ", userInfo=" + userInfo +
                '}';
    }
}