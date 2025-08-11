package com.gongxiang.zhenxuan.user.api.dto;

import javax.validation.constraints.NotBlank;

/**
 * 用户登录请求DTO
 *
 * @author gongxiang-zhenxuan
 */
public class UserLoginDTO {

    @NotBlank(message = "微信code不能为空")
    private String code;

    private String encryptedData;

    private String iv;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    @Override
    public String toString() {
        return "UserLoginDTO{" +
                "code='" + code + '\'' +
                ", encryptedData='" + encryptedData + '\'' +
                ", iv='" + iv + '\'' +
                '}';
    }
}