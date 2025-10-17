package com.btea.aurorabackendassessmentuploadsite.commom.convention.exception;

import com.btea.aurorabackendassessmentuploadsite.commom.convention.errorcode.BaseErrorCode;;

/**
 * @Author: TwentyFiveBTea
 * @Date: 2025/10/17 17:59
 * @Description: 客户端异常
 */
public class ClientException extends AbstractException{

    public ClientException(String message) {
        super(message, null, BaseErrorCode.CLIENT_ERROR);
    }

    public ClientException(String message, Throwable throwable) {
        super(message, throwable, BaseErrorCode.CLIENT_ERROR);
    }
}
