/*
 * Copyright (c) 2022-2025 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.common.error;

/**
 * 可进行错误描述的
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
public interface IErrorDescribable extends IErrorDefinable {
    /**
     * 因为 ... 原因导致的
     *
     * @param message 详细消息
     * @return 自定义业务异常
     */
    default IErrorDescribable becauseOf(String message) {
        return ErrorContext.instance().reset().mark(this).becauseOf(message);
    }

    /**
     * 因为 ... 原因导致的
     *
     * @param messagePattern 原因待格式化字符串
     * @param params         参数（支持最后1个参数为Throwable）
     * @return 自定义业务异常
     */
    default IErrorDescribable becauseOf(String messagePattern, final Object... params) {
        return ErrorContext.instance().reset().mark(this).becauseOf(messagePattern, params);
    }

    /**
     * 转为业务异常
     *
     * @return 业务异常
     */
    default RuntimeException toException() {
        return ErrorContext.instance().reset().mark(this).toException();
    }
}
