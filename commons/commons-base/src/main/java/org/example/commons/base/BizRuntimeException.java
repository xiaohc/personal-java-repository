/*
 * Copyright (c) 2022-2025 xiaohongchao.All Rights Reserved.
 */

package org.example.commons.base;

/**
 * 业务必检异常
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
public class BizRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -1537395265357977271L;

    public BizRuntimeException() {
        super();
    }

    public BizRuntimeException(String message) {
        super(message);
    }

    public BizRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizRuntimeException(Throwable cause) {
        super(cause);
    }
}
