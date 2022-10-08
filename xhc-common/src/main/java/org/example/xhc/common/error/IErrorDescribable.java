/*
 * Copyright (c) 2022-2025 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.common.error;

import java.io.Serializable;

/**
 * 可被描述的错误
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
public interface IErrorDescribable extends Serializable {
    /**
     * 获取标识码
     *
     * @return 标识码
     */
    int getCode();

    /**
     * 获取描述信息
     *
     * @return 描述信息
     */
    String getMessage();
}
