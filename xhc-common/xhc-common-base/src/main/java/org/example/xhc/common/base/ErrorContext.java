/*
 * Copyright (c) 2022-2025 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.common.base;

import lombok.Getter;

import java.io.Serializable;
import java.util.Optional;

/**
 * 错误上下文
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
@Getter
public class ErrorContext implements Serializable {
    private static final long serialVersionUID = 800312585448987400L;

    /**
     * 当前操作系统的换行符
     */
    private static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");

    /**
     * 性能增强，兼顾多线程
     */
    private static final ThreadLocal<ErrorContext> LOCAL = new ThreadLocal<>();

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误消息
     */
    private String message;

    /**
     * 错误原因
     */
    private String reason;

    /**
     * 如果是异常引起，记录对应异常
     */
    private Throwable cause;

    /**
     * 构成错误链，可以追溯错误源
     */
    private ErrorContext previous;

    /**
     * 防止外部实例化
     */
    private ErrorContext() {
    }

    /**
     * 从ThreadLocal取出已经实例化的ErrorContext，或者实例化一个ErrorContext放入ThreadLocal
     *
     * @return ErrorContext实例
     */
    public static ErrorContext instance() {
        ErrorContext context = LOCAL.get();
        if (context == null) {
            context = new ErrorContext();
            LOCAL.set(context);
        }
        return context;
    }

    /**
     * 创建一个包装了原有ErrorContext的新ErrorContext
     *
     * @return 新的ErrorContext实例
     */
    public ErrorContext store() {
        ErrorContext newContext = new ErrorContext();
        newContext.previous = this;
        LOCAL.set(newContext);
        return LOCAL.get();
    }

    /**
     * 剥离出当前ErrorContext的内部ErrorContext
     *
     * @return 剥离出的ErrorContext对象
     */
    public ErrorContext recall() {
        if (previous != null) {
            LOCAL.set(previous);
            previous = null;
        }
        return LOCAL.get();
    }

    /**
     * 从ThreadLocal中移除
     */
    public void remove() {
        LOCAL.remove();
    }

    /**
     * 重置原ErrorContext内容
     */
    public ErrorContext reset() {
        code = null;
        message = null;
        reason = null;
        cause = null;
        previous = null;
        return this;
    }

    /**
     * 记录错误信息
     *
     * @param errorRecord 错误记录
     * @return ErrorContext对象
     */
    public ErrorContext mark(IRecordable errorRecord) {
        Optional.ofNullable(errorRecord)
                .ifPresent(v -> {
                    this.code = v.getCode();
                    this.message = v.getMessage();
                });
        return this;
    }

    /**
     * 设置错误原因
     *
     * @param reason 原因
     * @return ErrorContext对象
     */
    public ErrorContext becauseOf(String reason) {
        this.reason = reason;
        return this;
    }

    /**
     * 设置引起错误的异常信息
     *
     * @param cause 异常
     * @return ErrorContext对象
     */
    public ErrorContext cause(Throwable cause) {
        this.cause = cause;
        return this;
    }

    /**
     * 转为业务异常
     *
     * @return 业务异常
     */
    public BizRuntimeException toException() {
        return new BizRuntimeException(this);
    }

    @Override
    public String toString() {
        StringBuilder description = new StringBuilder();

        // code
        if (code != null) {
            description.append(LINE_SEPARATOR);
            description.append("### The error code is ");
            description.append(code);
        }

        // message
        if (this.message != null) {
            description.append(LINE_SEPARATOR);
            description.append("### ");
            description.append(this.message);
        }

        // reason
        if (reason != null) {
            description.append(LINE_SEPARATOR);
            description.append("### The reason for the error is ");
            description.append(reason);
        }

        // cause
        if (cause != null) {
            description.append(LINE_SEPARATOR);
            description.append("### Cause: ");
            description.append(cause);
        }

        return description.toString();
    }
}
