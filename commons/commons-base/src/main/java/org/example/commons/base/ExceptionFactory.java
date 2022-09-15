/*
 * Copyright (c) 2022-2025 xiaohongchao.All Rights Reserved.
 */

package org.example.commons.base;

/**
 * 异常工厂
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
public final class ExceptionFactory {
    /**
     * 防止实例化
     */
    private ExceptionFactory() {
    }

    public static RuntimeException wrapException(String message, Exception e) {
        return new BizRuntimeException(message, e);
    }
}
