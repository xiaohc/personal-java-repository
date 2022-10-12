package org.example.xhc.common.validation;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

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
        return error -> {
            if (!expression) {
                throw error.toException();
            }
        };
    }

    /**
     * 验证对象是null，如果对象不为null，抛出免检异常
     *
     * @param object 对象
     * @return 一个函数接口实现
     * @throws RuntimeException 如果对象不为null，抛出免检异常
     */
    public static IExpectHandle isNull(final Object object) {
        return error -> {
            if (object != null) {
                throw error.toException();
            }
        };
    }

    /**
     * 验证对象不为null，如果对象是null，抛出免检异常
     *
     * @param object 对象
     * @return 一个函数接口实现
     * @throws RuntimeException 如果对象是null，抛出免检异常
     */
    public static IExpectHandle notNull(final Object object) {
        return error -> {
            if (object == null) {
                throw error.toException();
            }
        };
    }

    /**
     * 验证文本为空，如果文本不为空，抛出免检异常
     *
     * @param text 要检查的字符串
     * @return 一个函数接口实现
     * @throws RuntimeException 如果文本不为空，抛出免检异常
     */
    public static IExpectHandle isBlank(final String text) {
        return error -> {
            if (!StringUtils.isBlank(text)) {
                throw error.toException();
            }
        };
    }

    /**
     * 验证文本不为空，如果文本为空，抛出免检异常
     *
     * @param text 要检查的字符串
     * @return 一个函数接口实现
     * @throws RuntimeException 如果文本为空，抛出免检异常
     */
    public static IExpectHandle isNotBlank(final String text) {
        return error -> {
            if (!StringUtils.isNotBlank(text)) {
                throw error.toException();
            }
        };
    }

    /**
     * 验证数组不为空，如果数组为空，抛出免检异常
     *
     * @param object 要检查的对象，支持集合、数组、Map
     * @return 一个函数接口实现
     * @throws RuntimeException 如果数组为空，抛出免检异常
     */
    public static IExpectHandle notEmpty(final Object object) {
        return error -> {
            if (ObjectUtils.isEmpty(object)) {
                throw error.toException();
            }
        };
    }
}
