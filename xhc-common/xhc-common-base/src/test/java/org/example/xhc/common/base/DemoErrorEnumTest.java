/*
 * Copyright (c) 2022-2025 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.common.base;


import org.junit.jupiter.api.Test;

import static org.example.xhc.common.base.DemoErrorEnum.REQUEST_ERROR;

/**
 * TODO
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
class DemoErrorEnumTest {

    @Test
    void testAssert() {
//        Optional<IRecordable> iErrorContext = REQUEST_ERROR.assertIsTrue(1 == 1);
//        System.out.println(iErrorContext);

        // 通过AssertJ改进这个语法糖
        // Assert.isTrue(1 == 1).failed
        // Check.expectTrue().ifNot().becauseOf().throw();
//        REQUEST_ERROR.assertIsTrue(1 == 1).ifPresent(v -> v.getCode());
//        throw mark(REQUEST_ERROR).becauseOf("错误的输入内容： 0").failed();
        throw REQUEST_ERROR.becauseOf("错误的输入内容： {} 中包含了 {}", "#ok", "#", new RuntimeException("单测异常"));
//        REQUEST_ERROR.ifIn().ifPresent(v -> {
//            throw v.becauseOf("错误的输入内容： 0");
//        });
    }
}