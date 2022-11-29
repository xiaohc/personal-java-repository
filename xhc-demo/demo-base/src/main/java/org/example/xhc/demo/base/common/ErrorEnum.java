/*
 * Copyright (c) 2022 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.demo.base.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 标准错误码
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum ErrorEnum implements IResultEnum {
    /**
     * 系统内部错误
     */
    INTERNAL_SERVER_ERROR(9999, "System internal error"),

    /**
     * 无效结果
     */
    INVALID_RESULT_ERROR(9000, "Invalid value returned error"),

    /**
     * 结果不能为空
     */
    NULL_RESULT_ERROR(9001, "Null value returned error"),
    ;

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 错误消息
     */
    private final String message;
}
