/*
 * Copyright (c) 2022-2025 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.stream;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.val;
import org.example.xhc.common.util.JacksonUtils;
import org.example.xhc.entity.Student;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.stream.Collectors.*;

/**
 * 范例
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
class StreamGroupTest {

    @Test
    void testGroup() {
        val students = JacksonUtils.fromResource("json/student_array.json", new TypeReference<List<Student>>() {
        });

        assert students != null;
        val ret =
                students.stream()
                        .collect(groupingBy(Student::getSex, mapping(Student::getName, joining(",", "[", "]"))));

        val str = JacksonUtils.toYaml(ret);
        System.out.println(str);
    }

    @Test
    void testCollector() {
        val students = JacksonUtils.fromResource("json/student_array.json", new TypeReference<List<Student>>() {
        });

        assert students != null;
        val ret =
                students.stream()
                        .collect(groupingBy(Student::getSex, mapping(Student::getName, toList())));

        val str = JacksonUtils.toYaml(ret);
        System.out.println(str);
    }

    @Test
    void testCollector2() {
        val students = JacksonUtils.fromResource("json/student_array.json", new TypeReference<List<Student>>() {
        });

        assert students != null;
        val ret =
                students.stream()
                        .collect(groupingBy(Student::getSex,
                                groupingBy(Student::getAge, mapping(Student::getName, toList()))));

        val str = JacksonUtils.toYaml(ret);
        System.out.println(str);
    }
}