/*
 * Copyright (c) 2022-2025 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.common.base;

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
public enum StdErrorEnum implements ICanThrow {
    /**
     * 请求内容错误
     */
    REQUEST_ERROR(400, "请求内容错误"),

    /**
     * 应答内容错误
     */
    REPLY_ERROR(501, "应答内容错误"),
    ;

    /**
     * 错误码
     */
    private final int code;

    /**
     * 错误消息
     */
    private final String message;
}
