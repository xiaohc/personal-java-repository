/*
 * Copyright (c) 2022 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.demo.base.common;

import org.example.xhc.demo.base.util.ErrorContext;

import java.io.Serializable;

/**
 * 定义应答结果
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
public interface IResultEnum extends Serializable {
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
     * 因为 ... 原因导致的
     * 说明错误原因
     *
     * @param reason 错误原因
     * @return 错误上下文
     */
    default ErrorContext as(String reason) {
        return ErrorContext.of(this, reason);
    }

    /**
     * 因为 ... 原因导致的
     * 说明错误原因
     *
     * @param reasonPattern 原因待格式化字符串
     * @param params        格式化参数（支持最后1个参数为Throwable）
     * @return 错误上下文
     */
    default ErrorContext as(String reasonPattern, final Object... params) {
        return ErrorContext.of(this, reasonPattern, params);
    }
}
