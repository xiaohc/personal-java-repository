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
import org.junit.jupiter.api.Test;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.xhc.demo.base.common.ErrorEnum.RESULT_CONTENT_ERROR;

/**
 * 测试Result类
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
class ResultValidateTest {

    final List<Student> students = JacksonUtils.fromResource("json/student_array.json", new TypeReference<List<Student>>() {
    });

    @Test
    void validating() {
        final Student value = students.get(0);
        value.setName("");
        final Result<Student> result = Result.of(value).validating().apply(RESULT_CONTENT_ERROR.as("validate error"));

        assertThat(result).isInstanceOf(Result.Failure.class);
        assertThat(result.isFailure()).isTrue();
        assertThat(result.failureValue().getCode()).isEqualTo(9002);
    }

    @Test
    void validatingNull() {
        final Result<Student> result = Result.of((Student) null).validating().apply(RESULT_CONTENT_ERROR.as("validate error"));

        assertThat(result).isInstanceOf(Result.Failure.class);
        assertThat(result.isFailure()).isTrue();
        assertThat(result.failureValue().getCode()).isEqualTo(9002);
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
        @NotBlank(message = "姓名不能为空")
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