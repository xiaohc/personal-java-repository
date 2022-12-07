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
    INTERNAL_SERVER_ERROR("BASE-COMM-999", "System internal error"),

    /**
     * Result实例创建错误
     */
    RESULT_CREATION_ERROR("BASE-COMM-901", "Result instance creation error"),

    /**
     * Result实例内容错误
     */
    RESULT_CONTENT_ERROR("BASE-COMM-902", "Result instance content error"),

    /**
     * Result实例调用错误
     */
    RESULT_INVOKE_ERROR("BASE-COMM-903", "Result instance calls the wrong method"),

    /**
     * Result实例调用Map出错
     */
    RESULT_MAP_ERROR("BASE-COMM-904", "When calling method map() on Result instance, throw an exception"),
    ;

    /**
     * 错误码
     */
    private final String code;

    /**
     * 错误消息
     */
    private final String message;
}
