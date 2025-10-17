package com.btea.aurorabackendassessmentuploadsite.commom.convention.exception;

import lombok.Getter;

/**
 * @Author: TwentyFiveBTea
 * @Date: 2025/10/17 17:59
 * @Description: 基础异常类
 */
@Getter
public class BaseException extends RuntimeException {
    private final String code;

    public BaseException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BaseException(String message) {
        super(message);
        this.code = null;
    }
}
