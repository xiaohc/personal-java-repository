/*
 * Copyright (c) 2022 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.demo.base.util;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.xhc.common.util.JacksonUtils;
import org.example.xhc.demo.base.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.api.function.ThrowingSupplier;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.xhc.common.constant.SystemConstants.LINE_SEPARATOR;
import static org.example.xhc.demo.base.common.ErrorEnum.INTERNAL_SERVER_ERROR;
import static org.example.xhc.demo.base.common.ErrorEnum.RESULT_CONTENT_ERROR;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 测试Result类
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
class ResultAssertTest {

    final List<Student> students = JacksonUtils.fromResource("json/student_array.json", new TypeReference<List<Student>>() {
    });

    @Test
    void asserting() {
        final Result<List<Student>> result = Result.of(students).asserting(List::isEmpty);

        assertThat(result).isInstanceOf(Result.Failure.class);
        assertThat(result.isFailure()).isTrue();
        assertThat(result.failureValue()).isEqualTo(RESULT_CONTENT_ERROR.as(""));
    }

    @Test
    void orThrow1() {
        Executable validate = () -> Result.of(students).asserting(List::isEmpty).orThrow();

        BusinessException exception = assertThrows(BusinessException.class, validate);
        assertThat(exception).hasMessage(LINE_SEPARATOR + ">>> Result instance content error" +
                LINE_SEPARATOR + ">>> The error code is 9002");
    }

    @Test
    void orThrow2() {
        Executable validate = () -> Result.of(students).asserting(List::isEmpty).orThrow(INTERNAL_SERVER_ERROR.as("测试断言"));

        BusinessException exception = assertThrows(BusinessException.class, validate);
        assertThat(exception).hasMessage(LINE_SEPARATOR + ">>> System internal error" +
                LINE_SEPARATOR + ">>> The error code is 9999" +
                LINE_SEPARATOR + ">>> 测试断言");
    }

    @Test
    void orThrowNothing1() {
        Executable validate = () -> Result.of(students).asserting(v -> v.size() == 3).orThrow();
        assertDoesNotThrow(validate);
    }

    @Test
    void orThrowNothing2() {

        ThrowingSupplier<Result<?>> supplier = () -> Result.of(students).asserting(v -> v.size() == 3).orThrow();

        final Result<?> result = assertDoesNotThrow(supplier);
        assertThat(result).isNotNull()
                .isInstanceOf(Result.Success.class);
        assertThat(result.get())
                .isEqualTo(students);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    private static class Student {
        /**
         * 学号
         */
        private String no;

        /**
         * 姓名
         */
        private String name;

        /**
         * 性别
         */
        private String sex;

        /**
         * 年龄
         */
        private Integer age;

        /**
         * 生日
         */
        private LocalDateTime birthday;

        /**
         * 班级号
         */
        private String classNo;
    }
}