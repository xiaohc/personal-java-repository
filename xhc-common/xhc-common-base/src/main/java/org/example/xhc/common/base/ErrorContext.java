/*
 * Copyright (c) 2022-2025 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.common.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 错误上下文
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorContext implements Serializable {
    private static final long serialVersionUID = 800312585448987400L;

    /**
     * 性能增强，兼顾多线程
     */
    private static final ThreadLocal<ErrorContext> LOCAL = new ThreadLocal<>();

    /**
     * 错误码
     */
    private int code;

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
     * @return
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
     * @return 新的ErrorContext
     */
    public ErrorContext store() {
        ErrorContext newContext = new ErrorContext();
        newContext.previous = this;
        LOCAL.set(newContext);
        return LOCAL.get();
    }

    /**
     * 剥离出当前ErrorContext的内部ErrorContext
     * @return 剥离出的ErrorContext对象
     */
    public ErrorContext recall() {
        if (previous != null) {
            LOCAL.set(previous);
            previous = null;
        }
        return LOCAL.get();
    }

    public ErrorContext reset() {
        resource = null;
        activity = null;
        object = null;
        message = null;
        sql = null;
        cause = null;
        LOCAL.remove();
        return this;
    }

    public ErrorContext resetBy(DemoErrorEnum errorEnum) {
        return new ErrorContext();
    }

    public BizRuntimeException failed() {
        return new BizRuntimeException(this);
    }

    /**
     * @param reason 原因
     * @return
     */
    public ErrorContext becauseOf(String reason) {
        return new ErrorContext();
    }
}
