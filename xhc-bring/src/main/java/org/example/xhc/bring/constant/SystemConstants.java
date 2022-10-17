/*
 * Copyright (c) 2022-2025 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.bring.constant;

import org.apache.commons.lang3.StringUtils;

/**
 * 系统常量
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
public final class SystemConstants {
    /**
     * 当前操作系统的换行符
     * 改用System.lineSeparator() ，因为它不需要权限检查
     */
    public static final String LINE_SEPARATOR;

    static {
        String lineSeparator = System.lineSeparator();
        LINE_SEPARATOR = lineSeparator == null ? StringUtils.LF : lineSeparator;
    }

    /**
     * 防止外部实例化
     */
    private SystemConstants() {
    }
}
