/*
 * Copyright (c) 2022-2025 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.common.base.checker;

/**
 * 断言类
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
public final class Assert {
    /**
     * 防止实例化
     */
    private Assert() {
    }

    public static void isTrue(boolean b, RuntimeException e) {
        if (b) {
            throw e;
        }
    }

}
