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
     * Result实例构建错误
     */
    RESULT_CONSTRUCTION_ERROR(9000, "Result instance construction error"),

    /**
     * Result实例内容错误
     */
    RESULT_CONTENT_ERROR(9001, "Result instance content error"),

    /**
     * Result实例调用错误
     */
    RESULT_INVOKE_ERROR(9002, "Result instance calls the wrong method"),

    /**
     * 结果不能为空
     */
    RESULT_NULL_VALUE_ERROR(9002, "Null value returned error"),
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
