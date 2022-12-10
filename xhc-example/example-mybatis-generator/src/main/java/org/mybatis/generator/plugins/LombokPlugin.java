/*
 * Copyright (c) 2022 xiaohongchao.All Rights Reserved.
 */

package org.mybatis.generator.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.List;

/**
 * 使用方法：
 * 把编译好的 LombokPlugin.class 文件放到 mybatis-generator-core-x.y.z.jar 包的 org\mybatis\generator\plugins\ 下即可
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
public class LombokPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> list) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if (isHasLombok()) {
            addLombokContent(topLevelClass);
        }

        addFileHeader(topLevelClass, introspectedTable);

        return true;
    }

    private void addFileHeader(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        topLevelClass.addJavaDocLine("/**");

        addJavaDocLineOnElement(topLevelClass, introspectedTable.getRemarks());

        topLevelClass.addJavaDocLine(" * " + introspectedTable.getFullyQualifiedTable());
        topLevelClass.addJavaDocLine(" * @author xiaohongchao");
        topLevelClass.addJavaDocLine(" * @since 1.0.0");

        topLevelClass.addJavaDocLine(" */");
    }

    private void addLombokContent(TopLevelClass topLevelClass) {
        // 添加 lombok 依赖
        topLevelClass.addImportedType("lombok.Data");
        topLevelClass.addImportedType("lombok.Builder");
        topLevelClass.addImportedType("lombok.NoArgsConstructor");
        topLevelClass.addImportedType("lombok.AllArgsConstructor");

        // 添加 lombok 注解
        topLevelClass.addAnnotation("@Data");
        topLevelClass.addAnnotation("@Builder");
        topLevelClass.addAnnotation("@NoArgsConstructor");
        topLevelClass.addAnnotation("@AllArgsConstructor");
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        field.addJavaDocLine("/**");
        addJavaDocLineOnElement(field, introspectedColumn.getRemarks());
        field.addJavaDocLine(" */");
        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        // 添加Mapper的import
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Mapper"));
        // 添加Mapper的注解
        interfaze.addAnnotation("@Mapper");
        return true;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) { // 不生成getter
        return !isHasLombok();
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) { // 不生成setter
        return !isHasLombok();
    }

    private boolean isHasLombok() {
        return Boolean.parseBoolean(properties.getProperty("hasLombok", "false"));
    }

    private void addJavaDocLineOnElement(JavaElement element, String remarks) {
        if (StringUtility.stringHasValue(remarks)) {
            String[] remarkLines = remarks.split(System.getProperty("line.separator"));
            for (String remarkLine : remarkLines) {
                element.addJavaDocLine(" * " + remarkLine);
            }
        }
    }
}

