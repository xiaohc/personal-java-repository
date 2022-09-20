/*
 * Copyright (c) 2022-2025 xiaohongchao.All Rights Reserved.
 */

package org.example.demo.commons.base;


import org.junit.jupiter.api.Test;

import java.util.Optional;

/**
 * TODO
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
class DemoErrorEnumTest {

    @Test
    void testAssert() {
        Optional<IErrorContext> iErrorContext = DemoErrorEnum.REQUEST_ERROR.assertIsTrue(1 == 1);
        System.out.println(iErrorContext);

        // 通过AssertJ改进这个语法糖
        // Assert.isTrue(1 == 1).failed
        // Check.expectTrue().ifNot().becauseOf().throw();
        DemoErrorEnum.REQUEST_ERROR.assertIsTrue(1 == 1).ifPresent(v -> v.getCode());
    }
}