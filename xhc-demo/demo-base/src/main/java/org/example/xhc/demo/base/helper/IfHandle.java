/*
 * Copyright (c) 2022-2025 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.demo.base.helper;

import org.example.xhc.demo.base.reply.ErrorContext;

import java.util.Objects;

/**
 * if语法糖的类型定义
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
@FunctionalInterface
public interface IfHandle {
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
    void thenThrow(ErrorContext error);

    /**
     * 串联多个If语法糖
     *
     * @param other 其他If语法糖
     * @return If语法糖
     */
    default IfHandle or(IfHandle other) {
        Objects.requireNonNull(other);
        return (context) -> {
            thenThrow(context);
            other.thenThrow(context);
        };
    }
}
