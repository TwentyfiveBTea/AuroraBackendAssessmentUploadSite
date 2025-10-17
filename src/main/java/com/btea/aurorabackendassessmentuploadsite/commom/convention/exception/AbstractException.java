package com.btea.aurorabackendassessmentuploadsite.commom.convention.exception;


import com.btea.aurorabackendassessmentuploadsite.commom.convention.errorcode.IErrorCode;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * @Author: TwentyFiveBTea
 * @Date: 2025/10/17 17:59
 * @Description: 抽象异常
 */
@Getter
public abstract class AbstractException extends RuntimeException {

    public final String errorCode;

    public final String errorMessage;

    public AbstractException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message, throwable);
        this.errorCode = errorCode.code();
        this.errorMessage = Optional.ofNullable(StringUtils.hasLength(message) ? message : null).orElse(errorCode.message());
    }
}
