package org.example.xhc.common.validation;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

/**
 * 业务断言工具类
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
public final class BusinessAssert {
    /**
     * 防止实例化
     */
    private BusinessAssert() {
    }

    /**
     * 验证参数条件是否为true，如果条件表达式的计算结果为false，抛出免检异常
     *
     * @param expression 一个布尔表达式
     * @param e          抛出的免检异常
     * @throws RuntimeException 如果表达式为false，抛出免检异常
     */
    public static void isTrue(final boolean expression, final RuntimeException e) {
        if (!expression) {
            throw e;
        }
    }

    /**
     * 验证对象是null，如果对象不为null，抛出免检异常
     *
     * @param object 对象
     * @param e      抛出的免检异常
     * @throws RuntimeException 如果对象不为null，抛出免检异常
     */
    public static void isNull(final Object object, final RuntimeException e) {
        if (object != null) {
            throw e;
        }
    }

    /**
     * 验证对象不为null，如果对象是null，抛出免检异常
     *
     * @param object 对象
     * @param e      抛出的免检异常
     * @throws RuntimeException 如果对象是null，抛出免检异常
     */
    public static void notNull(final Object object, final RuntimeException e) {
        if (object == null) {
            throw e;
        }
    }

    /**
     * 验证文本为空，如果文本不为空，抛出免检异常
     *
     * @param text 要检查的字符串
     * @param e    抛出的免检异常
     * @throws RuntimeException 如果文本不为空，抛出免检异常
     */
    public static void isBlank(final String text, final RuntimeException e) {
        if (!StringUtils.isBlank(text)) {
            throw e;
        }
    }

    /**
     * 验证文本不为空，如果文本为空，抛出免检异常
     *
     * @param text 要检查的字符串
     * @param e    抛出的免检异常
     * @throws RuntimeException 如果文本为空，抛出免检异常
     */
    public static void isNotBlank(final String text, final RuntimeException e) {
        if (!StringUtils.isNotBlank(text)) {
            throw e;
        }
    }

    /**
     * 验证数组不为空，如果数组为空，抛出免检异常
     *
     * @param array 要检查的数组
     * @param e     抛出的免检异常
     * @throws RuntimeException 如果数组为空，抛出免检异常
     */
    public static void notEmpty(final Object[] array, final RuntimeException e) {
        if (ObjectUtils.isEmpty(array)) {
            throw e;
        }
    }

    /**
     * 验证集合不为空，如果集合为空，抛出免检异常
     *
     * @param collection 要检查的集合
     * @param e          抛出的免检异常
     * @throws RuntimeException 如果集合为空，抛出免检异常
     */
    public static void notEmpty(final Collection<?> collection, final RuntimeException e) {
        if (collection == null || collection.isEmpty()) {
            throw e;
        }
    }
}
