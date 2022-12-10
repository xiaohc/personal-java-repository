/*
 * Copyright (c) 2022 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.xhc.entity.type.SexEnum;

import java.time.LocalDateTime;

/**
 * 教师
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Teacher {
    /**
     * 教师编号
     */
    private String no;

    /**
     * 姓名
     */
    private String name;

    /**
     * 职称
     */
    private String title;

    /**
     * 性别
     */
    private SexEnum sex;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 生日
     */
    private LocalDateTime birthday;
}
