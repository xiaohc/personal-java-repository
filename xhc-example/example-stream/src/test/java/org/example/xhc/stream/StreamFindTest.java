/*
 * Copyright (c) 2022-2025 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.stream;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 范例
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
class StreamFindTest {

    @Test
    void testMin() {
        val ret =
                Stream.of("a", "1abc", "abc1")
                        .min(Comparator.naturalOrder())
                        .get();

        assertThat(ret).isEqualTo("1abc");
    }
}