package com.gongxiang.zhenxuan.common.exception;

import com.gongxiang.zhenxuan.common.result.Result;
import com.gongxiang.zhenxuan.common.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * 全局异常处理器
 *
 * @author gongxiang-zhenxuan
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.warn("业务异常: code={}, message={}", e.getCode(), e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理参数校验异常 - @RequestBody
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder errorMessage = new StringBuilder();
        
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMessage.append(fieldError.getField())
                      .append(": ")
                      .append(fieldError.getDefaultMessage())
                      .append("; ");
        }
        
        String message = errorMessage.toString();
        log.warn("参数校验异常: {}", message);
        return Result.error(ResultCode.PARAM_ERROR.getCode(), message);
    }

    /**
     * 处理参数校验异常 - @ModelAttribute
     */
    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder errorMessage = new StringBuilder();
        
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMessage.append(fieldError.getField())
                      .append(": ")
                      .append(fieldError.getDefaultMessage())
                      .append("; ");
        }
        
        String message = errorMessage.toString();
        log.warn("参数绑定异常: {}", message);
        return Result.error(ResultCode.PARAM_ERROR.getCode(), message);
    }

    /**
     * 处理参数校验异常 - @RequestParam
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> handleConstraintViolationException(ConstraintViolationException e) {
        StringBuilder errorMessage = new StringBuilder();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        
        for (ConstraintViolation<?> violation : violations) {
            errorMessage.append(violation.getPropertyPath())
                      .append(": ")
                      .append(violation.getMessage())
                      .append("; ");
        }
        
        String message = errorMessage.toString();
        log.warn("约束校验异常: {}", message);
        return Result.error(ResultCode.PARAM_ERROR.getCode(), message);
    }

    /**
     * 处理非法参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("非法参数异常: {}", e.getMessage());
        return Result.error(ResultCode.PARAM_ERROR.getCode(), e.getMessage());
    }

    /**
     * 处理空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    public Result<Void> handleNullPointerException(NullPointerException e) {
        log.error("空指针异常", e);
        return Result.error(ResultCode.SYSTEM_ERROR.getCode(), "系统内部错误");
    }

    /**
     * 处理其他未知异常
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("未知异常", e);
        return Result.error(ResultCode.SYSTEM_ERROR);
    }
}