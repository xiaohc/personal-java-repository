/*
 * Copyright (c) 2022 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.demo.base.helper;

import lombok.val;
import org.example.xhc.demo.base.reply.Result;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FunctionTest {
    @Test
    void testCheckErrors() {

        val test = Result.failure("test");

//        final Result.Success<String> success = new Result.Success<>("ok");
    }

    /* 一个多态高阶函数例子 */
    public static <T, U, V> Function<Function<U, V>, Function<Function<T, U>, Function<T, V>>> higherCompose() {
        return f -> g -> t -> f.apply(g.apply(t));
    }

    /* 赋予 lambda参数 一个有意义的名字 */
    public static <T, U, V> Function<Function<U, V>, Function<Function<T, U>, Function<T, V>>> higherCompose2() {
        return tuFunc -> uvFunc -> t -> tuFunc.apply(uvFunc.apply(t));
    }

    /* 给 lambda参数 补上类型信息 */
    public static <T, U, V> Function<Function<U, V>, Function<Function<T, U>, Function<T, V>>> higherCompose3() {
        return (Function<U, V> f) -> (Function<T, U> g) -> (T t) -> f.apply(g.apply(t));
    }

    @Test
    void testFunctionCompose() {
        Function<Double, Integer> f = t -> (int) (t * 3);
        Function<Long, Double> g = t -> t + 2.0;

        assertEquals(Integer.valueOf(9), f.apply((g.apply(1L))));
        assertEquals(Integer.valueOf(9), FunctionTest.<Long, Double, Integer>higherCompose().apply(f).apply(g).apply(1L));
    }
}

