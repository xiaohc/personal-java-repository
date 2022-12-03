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

import java.time.LocalDateTime;
import java.util.List;

/**
 * 测试Result类
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
class ResultMapTest {

    final List<Student> students = JacksonUtils.fromResource("json/student_array.json", new TypeReference<List<Student>>() {
    });

    @Test
    void map() {
        final Result<Student> studentResult = Result.of(students)
                .map(v -> v.get(0));
    }

    @Test
    void flatMap() {
        final Result<Student> studentResult = Result.of(students)
                .flatMap(v -> Result.of(v.get(0)));
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