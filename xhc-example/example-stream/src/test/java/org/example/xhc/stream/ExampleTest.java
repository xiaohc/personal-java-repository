/*
 * Copyright (c) 2022-2025 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.stream;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Stream;

import static java.lang.Character.isDigit;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * 范例
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
class ExampleTest {

    @Test
    void testUnion() {
        val ret = Stream.of(asList(1, 2, 3), asList(3, 4)).flatMap(Collection::stream).collect(toList());

        assertThat(ret).isEqualTo(asList(1, 2, 3, 3, 4));
    }

    @Test
    void testGroup() {
        val ret = Stream.of("a", "1abc", "abc1").filter(value -> isDigit(value.charAt(0))).collect(toList());

        assertThat(ret).isEqualTo(Collections.singletonList("1abc"));
    }

    @Test
    void testFilter() {
        val ret = Stream.of("a", "1abc", "abc1").filter(value -> isDigit(value.charAt(0))).collect(toList());

        assertThat(ret).isEqualTo(Collections.singletonList("1abc"));
    }

    @Test
    void testSort() {
        val ret = Stream.of("a2", "abc", "a").sorted(Comparator.naturalOrder()).collect(toList());

        assertThat(ret).isEqualTo(asList("a", "a2", "abc"));
    }
}