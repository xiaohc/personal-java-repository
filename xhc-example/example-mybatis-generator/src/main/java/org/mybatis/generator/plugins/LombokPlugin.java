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
import java.util.Optional;

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

    private void addFileHeader(JavaElement element, IntrospectedTable introspectedTable) {
        element.addJavaDocLine("/**");

        addJavaDocLineOnElement(element, introspectedTable.getRemarks());

        element.addJavaDocLine(" * " + introspectedTable.getFullyQualifiedTable());
        element.addJavaDocLine(" * ");
        element.addJavaDocLine(" * @author xiaohongchao");
        element.addJavaDocLine(" * @since 1.0.0");

        element.addJavaDocLine(" */");
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
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) { // 不生成getter
        // 是否生成 Setter 方法
        return !isHasLombok();
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) { // 不生成setter
        // 是否生成 Getter 方法
        return !isHasLombok();
    }

    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        // 添加Mapper的import
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Mapper"));
        // 添加Mapper的注解
        interfaze.addAnnotation("@Mapper");
        // 添加文件头注释
        addFileHeader(interfaze, introspectedTable);
        return true;
    }

    @Override
    public boolean clientInsertMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        // 添加 insert 方法注释
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 插入数据");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @param row 待插入数据");
        method.addJavaDocLine(" * @return 插入成功行数");
        method.addJavaDocLine(" */");
        return true;
    }

    @Override
    public boolean clientSelectAllMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        // 添加 selectAll 方法注释
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 查询当前业务所有数据");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @return 当前业务所有数据内容");
        method.addJavaDocLine(" */");
        return true;
    }

    @Override
    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        // 添加 selectByPrimaryKey 方法注释
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 按主键查询数据");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @param id 待查询数据的主键");
        method.addJavaDocLine(" * @return 当前业务数据内容");
        method.addJavaDocLine(" */");
        return true;
    }

    @Override
    public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        // 添加 updateByPrimaryKey 方法注释
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 按主键更新数据");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @param row 待更新数据内容");
        method.addJavaDocLine(" * @return 更新成功行数");
        method.addJavaDocLine(" */");
        return true;
    }

    @Override
    public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        // 添加 updateByPrimaryKey 方法注释
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 按主键更新数据");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @param row 待更新数据内容");
        method.addJavaDocLine(" * @return 更新成功行数");
        method.addJavaDocLine(" */");
        return true;
    }

    @Override
    public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        // 添加 deleteByPrimaryKey 方法注释
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * 按主键删除数据");
        method.addJavaDocLine(" * ");
        method.addJavaDocLine(" * @param id 待删除数据的主键");
        method.addJavaDocLine(" * @return 删除成功行数");
        method.addJavaDocLine(" */");
        return true;
    }

    /**
     * 读取配置参数：是否启用 Lombok 注解
     *
     * <plugin type="org.mybatis.generator.plugins.LombokPlugin">
     * <property name="hasLombok" value="true"/>
     * </plugin>
     *
     * @return true - 启用， false- 不启用（默认）
     */
    private boolean isHasLombok() {
        return Optional.ofNullable(properties)
                .map(v -> v.getProperty("hasLombok"))
                .map(Boolean::parseBoolean)
                .orElse(false);
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

