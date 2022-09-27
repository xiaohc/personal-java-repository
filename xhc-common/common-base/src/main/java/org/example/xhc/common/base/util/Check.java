package org.example.xhc.common.base.util;

/**
 * 检查断言类
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
public final class Check {
    /**
     * 防止实例化
     */
    private Check() {
    }

    public static void isTrue(boolean expression, RuntimeException e) {
        if (!expression) {
            throw e;
        }
    }


}
