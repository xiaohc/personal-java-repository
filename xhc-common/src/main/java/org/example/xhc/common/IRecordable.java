/*
 * Copyright (c) 2022-2025 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.common;

import java.io.Serializable;

/**
 * 值得记录的
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
public interface IRecordable extends Serializable {
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
