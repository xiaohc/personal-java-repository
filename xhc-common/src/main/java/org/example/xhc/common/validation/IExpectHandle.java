package org.example.xhc.common.validation;

import org.example.xhc.common.error.ErrorContext;

/**
 * 定义如何处理期望
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
@FunctionalInterface
public interface IExpectHandle {
    /**
     * 抛出免检异常或不执行任何操作
     * <p>
     * if代码的流式语法糖:
     * <p>
     * if (...) {
     * throw new RuntimeException("出现异常了")；
     * }
     * <p>
     *
     * @param error 错误
     * @throws RuntimeException 如果非预期结果，则抛出免检异常
     */
    void throwIfFailed(ErrorContext error);
}
