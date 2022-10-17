/*
 * Copyright (c) 2022 xiaohongchao.All Rights Reserved.
 */
package org.example.xhc.common.util;

import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * 文件工具类
 *
 * @author xiaohongchao
 * @since 1.0
 */
public final class FileUtils {

    /**
     * 私有化工具类构造函数
     */
    private FileUtils() {
    }

    /**
     * 读取文件内容
     *
     * @param path 文件路径
     * @return 文件内容
     */
    @SneakyThrows
    public static String readString(String path) {
        ClassPathResource rs = new ClassPathResource(path);
        try (InputStreamReader in = new InputStreamReader(rs.getInputStream(), StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(in)) {
            return bufferedReader
                    .lines()
                    .collect(Collectors.joining("\n"));
        }
    }
}
