/*
 * Copyright (c) 2022-2025 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.stream;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.val;
import org.example.xhc.common.util.JacksonUtils;
import org.example.xhc.stream.entity.Student;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

/**
 * 范例
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
class StreamFilterTest {

    @Test
    void testFilter() {
        val students = JacksonUtils.fromResource("json/student_array.json", new TypeReference<List<Student>>() {
        });

        assert students != null;
        val ret =
                students.stream()
                        .filter(this::isSevenYearOld)
                        .collect(toList());

        val str = JacksonUtils.toYaml(ret);
        System.out.println(str);
    }

    private boolean isSevenYearOld(Student student) {
        return student != null
                && Objects.equals(student.getAge(), 7);
    }
}