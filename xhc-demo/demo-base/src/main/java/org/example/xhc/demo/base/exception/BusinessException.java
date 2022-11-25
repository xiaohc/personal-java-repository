/*
 * Copyright (c) 2022-2025 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.demo.base.exception;

import lombok.Getter;
import org.example.xhc.demo.base.util.ErrorContext;

/**
 * 标准业务异常，业务服务中的业务相关异常的超类
 * 免检异常，不需要在方法或构造函数的throws子句中声明。
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
@Getter
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = -3546073653800972528L;

    /**
     * 错误上下文
     */
    private final ErrorContext errorContext;

    /**
     * 构造函数
     *
     * @param errorContext 错误上下文（保存以供以后通过getErrorContext()方法检索）。
     */
    public BusinessException(ErrorContext errorContext) {
        super(errorContext == null ? null : errorContext.toString());
        this.errorContext = errorContext;
    }

    /**
     * 构造函数
     *
     * @param errorContext 错误上下文（保存以供以后通过getErrorContext()方法检索）。
     * @param cause        原因（保存以供以后通过getCause()方法检索）。 （允许使用空值，表示原因不存在或未知。）
     */
    public BusinessException(ErrorContext errorContext, Throwable cause) {
        super(errorContext == null ? null : errorContext.toString(), cause);
        this.errorContext = errorContext;
    }

}
