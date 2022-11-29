/*
 * Copyright (c) 2022 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.demo.base.util;

import org.example.xhc.demo.base.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.xhc.common.constant.SystemConstants.LINE_SEPARATOR;
import static org.example.xhc.demo.base.util.If.isNull;
import static org.example.xhc.demo.base.util.If.isTrue;
import static org.example.xhc.demo.base.common.ErrorEnum.INTERNAL_SERVER_ERROR;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 工具类单元测试
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
class IfHandleTest {

    @Test
    void testOr1() {
        Executable validate = () ->
                isNull(null).or(isTrue(false))
                        .thenThrow(INTERNAL_SERVER_ERROR.as("测试语法糖函数: {}", "isTrue"));

        BusinessException exception = assertThrows(BusinessException.class, validate);

        assertThat(exception).hasMessage(LINE_SEPARATOR + ">>> System internal error" +
                LINE_SEPARATOR + ">>> The error code is 9999" +
                LINE_SEPARATOR + ">>> 测试语法糖函数: isTrue");
    }

    @Test
    void testOr2() {
        Executable validate = () ->
                isTrue(false).or(isNull(null))
                        .thenThrow(INTERNAL_SERVER_ERROR.as("测试语法糖函数: {}", "isTrue"));

        BusinessException exception = assertThrows(BusinessException.class, validate);

        assertThat(exception).hasMessage(LINE_SEPARATOR + ">>> System internal error" +
                LINE_SEPARATOR + ">>> The error code is 9999" +
                LINE_SEPARATOR + ">>> 测试语法糖函数: isTrue");
    }

    @Test
    void testOr3() {
        Executable validate = () ->
                isTrue(true).or(isNull(null))
                        .thenThrow(INTERNAL_SERVER_ERROR.as("测试语法糖函数: {}", "isTrue"));

        BusinessException exception = assertThrows(BusinessException.class, validate);

        assertThat(exception).hasMessage(LINE_SEPARATOR + ">>> System internal error" +
                LINE_SEPARATOR + ">>> The error code is 9999" +
                LINE_SEPARATOR + ">>> 测试语法糖函数: isTrue");
    }

    @Test
    void testOr4() {
        Executable validate = () ->
                isTrue(false).or(isNull(new Object()))
                        .thenThrow(INTERNAL_SERVER_ERROR.as("测试语法糖函数: {}", "isTrue"));

         assertDoesNotThrow(validate);
    }
}