/*
 * Copyright (c) 2022-2025 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.common.reply;

import java.io.Serializable;

/**
 * 定义应答中的内容
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
public interface IResponseContent extends Serializable {
    /**
     * 获取标识码
     *
     * @return 标识码
     */
    Integer getCode();

    /**
     * 获取描述信息
     *
     * @return 描述信息
     */
    String getMessage();

    /**
     * 说明错误原因
     * 因为 ... 原因导致的
     *
     * @param message 详细消息
     * @return 自定义业务异常
     */
    default ErrorContext as(String message) {
        return ErrorContext.instance().reset(this).description(message);
    }

    /**
     * 因为 ... 原因导致的
     *
     * @param messagePattern 原因待格式化字符串
     * @param params         参数（支持最后1个参数为Throwable）
     * @return 自定义业务异常
     */
    default ErrorContext as(String messagePattern, final Object... params) {
        return ErrorContext.instance().reset(this).description(messagePattern, params);
    }
}
