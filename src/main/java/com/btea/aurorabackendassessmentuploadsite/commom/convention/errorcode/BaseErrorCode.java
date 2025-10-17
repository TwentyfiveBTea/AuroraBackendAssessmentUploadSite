package com.btea.aurorabackendassessmentuploadsite.commom.convention.errorcode;

import lombok.AllArgsConstructor;

/**
 * @Author: TwentyFiveBTea
 * @Date: 2025/10/17 17:59
 * @Description: 基础错误码
 */
@AllArgsConstructor
public enum BaseErrorCode implements IErrorCode {

    // 成功状态响应码
    SUCCESS("0000000", "操作成功"),

    // 客户端错误码
    CLIENT_ERROR("0000100", "客户端异常"),

    // 服务端错误码
    SERVICE_ERROR("0000200", "服务端异常");

    private final String code;
    private final String message;

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
