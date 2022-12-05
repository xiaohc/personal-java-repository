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
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.xhc.demo.base.util.Result.success;

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

        assertThat(studentResult).isNotNull().isInstanceOf(Result.Success.class);
    }

    @Test
    void flatMap() {
        final Result<Student> studentResult = Result.of(students)
                .flatMap(v -> Result.of(v.get(0)));

        assertThat(studentResult).isNotNull().isInstanceOf(Result.Success.class);
    }

    @Test
    void testCompose() {
        Function<Integer, Integer> f1 = v -> v + 1;
        Function<Integer, Integer> f2 = v -> v + 1;
        Function<Integer, Integer> f3 = v -> v + 1;

        Function<Integer, Integer> composeFunction = v -> f1.apply(f2.apply(f3.apply(0)));
        final Integer ret = composeFunction.apply(0);
        assertThat(ret).isEqualTo(3);
    }

    @Test
    void testCompose1() {
        final Result<String> result = Result.of("1").map(Integer::valueOf).map(Long::valueOf).map(String::valueOf);

        assertThat(result).isNotNull().isInstanceOf(Result.Success.class);
        assertThat(result.get()).isEqualTo("1");
    }

    @Test
    void testColi() {
        Function<Integer, Function<Integer, Function<Integer, Integer>>> add = x -> y -> z -> x + y + z;
        final Integer ret = add.apply(3).apply(5).apply(7);

        assertThat(ret).isEqualTo(15);
    }

    @Test
    void testColi2() {
        Result<Integer> result1 = success(1);
        Result<Integer> result2 = success(2);
        Result<Integer> result3 = success(3);
        Result<Integer> result4 = success(4);
        Result<Integer> result5 = success(5);

        Result<Integer> result = result1
                .flatMap(p1 -> result2
                        .flatMap(p2 -> result3
                                .flatMap(p3 -> result4
                                        .flatMap(p4 -> result5
                                                .map(p5 -> compute(p1, p2, p3, p4, p5))))));

        assertThat(result).isNotNull().isInstanceOf(Result.Success.class);
        assertThat(result.get()).isEqualTo(15);
    }

    private int compute(int p1, int p2, int p3, int p4, int p5) {
        return p1 + p2 + p3 + p4 + p5;
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