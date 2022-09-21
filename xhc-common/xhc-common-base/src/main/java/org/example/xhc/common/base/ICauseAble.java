/*
 * Copyright (c) 2022-2025 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.common.base;

/**
 * 需要说明原因的
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
public interface ICauseAble extends IRecordable {
    /**
     * 因为 ... 原因导致的
     *
     * @param message 详细消息
     * @return 自定义业务异常
     */
    default RuntimeException becauseOf(String message) {
        return ErrorContext.mark(this).becauseOf(message).failed();
    }

    /**
     * 因为 ... 原因导致的
     *
     * @param message 详细消息
     * @param e       引起错误的异常
     * @return 自定义业务异常
     */
    default RuntimeException becauseOf(String message, Exception e) {
        return becauseOf(message);
    }

}
