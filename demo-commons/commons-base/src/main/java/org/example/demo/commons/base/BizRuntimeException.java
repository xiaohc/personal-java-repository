/*
 * Copyright (c) 2022-2025 xiaohongchao.All Rights Reserved.
 */

package org.example.demo.commons.base;

import lombok.Getter;

/**
 * 通用业务异常，业务服务中的业务相关异常的超类
 * 免检异常，不需要在方法或构造函数的throws子句中声明。
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
@Getter
public class BizRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -3546073653800972528L;

    /**
     * 错误上下文
     */
    private final ErrorContext errorContext;

    /**
     * 构造函数
     *
     * @param message 详细消息（保存以供以后通过getMessage()方法检索）。
     */
    public BizRuntimeException(String message) {
        this(message, null);
    }

    public BizRuntimeException(ErrorContext errorContext) {
        this(errorContext.getMessage(), null);
    }

    /**
     * 构造函数
     *
     * @param message 详细消息（保存以供以后通过getMessage()方法检索）。
     */
    public BizRuntimeException(String message, ErrorContext errorContext) {
        super(message);
        this.errorContext = errorContext;
    }

    /**
     * 构造函数
     *
     * @param message 详细消息（保存以供以后通过getMessage()方法检索）。
     * @param cause   原因（保存以供以后通过getCause()方法检索）。 （允许使用空值，表示原因不存在或未知。）
     */
    public BizRuntimeException(String message, ErrorContext errorContext, Throwable cause) {
        super(message, cause);
        this.errorContext = errorContext;
    }

    /**
     * 构造函数
     *
     * @param cause 原因（保存以供以后通过getCause()方法检索）。 （允许使用空值，表示原因不存在或未知。）
     */
    public BizRuntimeException(ErrorContext errorContext, Throwable cause) {
        super(cause);
        this.errorContext = errorContext;
    }
}
