/*
 * Copyright (c) 2022-2025 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.common.helper;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 格式化元组
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
public class FormattingTuple {
    private String message;
    private Object[] argArray;
    private Throwable throwable;
}
