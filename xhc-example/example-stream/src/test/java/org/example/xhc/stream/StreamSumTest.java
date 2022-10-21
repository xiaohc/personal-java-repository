/*
 * Copyright (c) 2022-2025 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.stream;

import lombok.val;
import org.example.xhc.common.util.JacksonUtils;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

/**
 * 范例
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
class StreamSumTest {

    @Test
    void testSum() {
        val ret =
                Stream.of(1, 2, 3)
                        .mapToInt(Integer::valueOf)
                        .sum();

        val str = JacksonUtils.toYaml(ret);
        System.out.println(str);
    }
}