/*
 * Copyright (c) 2022-2025 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.stream;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
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
class ExampleTest {

    @Test
    void testUnion() {
        List<Integer> together = Stream.of(asList(1, 2), asList(3, 4))
                .flatMap(Collection::stream)
                .collect(toList());

        assertThat(together).isEqualTo(asList(1, 2, 3, 4));
    }
}