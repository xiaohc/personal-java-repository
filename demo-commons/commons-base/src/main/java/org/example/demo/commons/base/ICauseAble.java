/*
 * Copyright (c) 2022-2025 xiaohongchao.All Rights Reserved.
 */

package org.example.demo.commons.base;

/**
 * 需要说明原因的
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
public interface ICauseAble {
    /**
     * 因为 ... 原因导致的
     *
     * @param message 详细消息
     * @return 自定义业务异常
     */
    default RuntimeException becauseOf(String message) {
        return new BizRuntimeException(message);
    }

    /**
     * 因为 ... 原因导致的
     *
     * @param message 详细消息
     * @param e       引起错误的异常
     * @return 自定义业务异常
     */
    default RuntimeException becauseOf(String message, Exception e) {
        return new BizRuntimeException(message, e);
    }

    /**
     * 因为 ... 原因导致的
     *
     * @param e 引起错误的异常
     * @return 自定义业务异常
     */
    default RuntimeException becauseOf(Exception e) {
        return new BizRuntimeException(e);
    }
}
