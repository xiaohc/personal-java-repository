/*
 * Copyright (c) 2022-2025 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.common.error;

import org.example.xhc.common.exception.BusinessException;
import org.example.xhc.common.validation.BusinessAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.io.RandomAccessFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 测试标准错误码的使用
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
class ErrorEnumTest {

    @Test
    void testValidate() {
        Executable validate = () -> {
            BusinessAssert.isTrue(new Object() == null,
                    ErrorEnum.REQUEST_ERROR.becauseOf("测试业务检查工具类: {}", "Validate"));
        };

        BusinessException exception = assertThrows(BusinessException.class, validate);
        // windows
        assertThat(exception).hasMessage("\r\n" +
                ">>> 请求内容错误\r\n" +
                ">>> The error code is 400\r\n" +
                ">>> The reason for the error is 测试业务检查工具类: Validate");
    }

    @Test
    void testAssert() {
        Executable validate = () -> {
            try {
                System.out.println("随机读取一段文件内容：");
                // 打开一个随机访问文件流，按只读方式
                RandomAccessFile randomFile = new RandomAccessFile("/error.file", "r");
                // 文件长度，字节数
                long fileLength = randomFile.length();
                // 读文件的起始位置
                int beginIndex = (fileLength > 4) ? 4 : 0;
                // 将读文件的开始位置移到beginIndex位置。
                randomFile.seek(beginIndex);
                byte[] bytes = new byte[10];
                int byteread = 0;
                // 一次读10个字节，如果文件内容不足10个字节，则读剩下的字节。
                // 将一次读取的字节数赋给byteread
                while ((byteread = randomFile.read(bytes)) != -1) {
                    System.out.write(bytes, 0, byteread);
                }
            } catch (IOException e) {
                throw ErrorEnum.REQUEST_ERROR.becauseOf("随机读取文件错误： 期望 {}， 实际 {}", "success", "failed", e);
            }
        };

        BusinessException exception = assertThrows(BusinessException.class, validate);
//        Optional<IRecordable> iErrorContext = REQUEST_ERROR.assertIsTrue(1 == 1);
//        System.out.println(iErrorContext);

        // 通过AssertJ改进这个语法糖
        // Assert.isTrue(1 == 1).failed
        // Check.expectTrue().ifNot().becauseOf().throw();
//        REQUEST_ERROR.assertIsTrue(1 == 1).ifPresent(v -> v.getCode());
//        throw mark(REQUEST_ERROR).becauseOf("错误的输入内容： 0").failed();
//        REQUEST_ERROR.ifIn().ifPresent(v -> {
//            throw v.becauseOf("错误的输入内容： 0");
//        });


    }
}