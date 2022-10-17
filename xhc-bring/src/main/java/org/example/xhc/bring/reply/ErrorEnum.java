/*
 * Copyright (c) 2022-2025 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.bring.reply;

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
public enum ErrorEnum implements IResponseContent {
    /**
     * 系统内部错误
     */
    INTERNAL_SERVER_ERROR(9999, "系统内部错误"),
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
