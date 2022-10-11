package org.example.xhc.common.validation;

/**
 * 业务验证工具类
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
public final class Expect {

    /**
     * 防止实例化
     */
    private Expect() {
    }

    /**
     * 验证参数条件是否为true，如果条件表达式的计算结果为false，抛出免检异常
     *
     * @param expression 一个布尔表达式
     * @return 一个函数接口实现
     * @throws RuntimeException 如果表达式为false，抛出免检异常
     */
    public static IExpectHandle isTrue(final boolean expression) {
        return (runtimeException) -> {
            if (!expression) {
                throw runtimeException;
            }
        };
    }

}
