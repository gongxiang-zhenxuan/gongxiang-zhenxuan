package com.gongxiang.zhenxuan.common.exception;

import com.gongxiang.zhenxuan.common.result.ResultCode;

/**
 * 业务异常类
 *
 * @author gongxiang-zhenxuan
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Integer code;

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    public BusinessException(ResultCode resultCode, String message) {
        super(message);
        this.code = resultCode.getCode();
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.code = 500;
    }

    public BusinessException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}