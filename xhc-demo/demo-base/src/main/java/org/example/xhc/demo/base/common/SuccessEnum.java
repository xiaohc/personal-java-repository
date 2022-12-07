/*
 * Copyright (c) 2022 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.demo.base.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 标准应答码
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum SuccessEnum implements IResultEnum {
    /**
     * 处理成功
     */
    SUCCESS("0", "success"),
    ;

    /**
     * 应答码
     */
    private final String code;

    /**
     * 应答消息
     */
    private final String message;
}
