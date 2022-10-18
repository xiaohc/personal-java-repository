/*
 * Copyright (c) 2022-2025 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.stream;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
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
class StreamSortTest {

    @Test
    void testSort() {
        val ret =
                Stream.of("a2", "abc", "a")
                        .sorted(Comparator.naturalOrder())
                        .collect(toList());

        assertThat(ret).isEqualTo(asList("a", "a2", "abc"));
    }
}