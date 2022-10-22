/*
 * Copyright (c) 2022-2025 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.stream;

import lombok.val;
import org.example.xhc.common.util.JacksonUtils;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * 范例
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
class StreamUnionTest {

    @Test
    void testUnionAll() {
        val ret =
                Stream.of(asList(1, 2, 3), asList(3, 4))
                        .flatMap(Collection::stream)
                        .collect(toList());

        assertThat(ret).isEqualTo(asList(1, 2, 3, 3, 4));

        val str = JacksonUtils.toYaml(ret);
        System.out.println(str);
    }

    @Test
    void testUnion() {
        val ret =
                Stream.of(asList(1, 2, 3), asList(3, 4))
                        .flatMap(Collection::stream)
                        .distinct()
                        .collect(toList());

        assertThat(ret).isEqualTo(asList(1, 2, 3, 4));

        val str = JacksonUtils.toYaml(ret);
        System.out.println(str);
    }

    @Test
    void testIntersection() {
        val ret =
                Stream.of(1, 2, 3)
                        .filter(v -> asList(3, 4).contains(v))
                        .collect(toList());

        val str = JacksonUtils.toYaml(ret);
        System.out.println(str);
    }
}