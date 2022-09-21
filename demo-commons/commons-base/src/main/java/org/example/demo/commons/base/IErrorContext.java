/*
 * Copyright (c) 2022-2025 xiaohongchao.All Rights Reserved.
 */

package org.example.demo.commons.base;

import java.io.Serializable;

/**
 * 错误上下文
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
public interface IErrorContext extends Serializable {
    /**
     * 获取错误码
     *
     * @return 错误码
     */
    int getCode();

    /**
     * 获取错误描述
     *
     * @return 错误描述
     */
    String getDescription();
}
