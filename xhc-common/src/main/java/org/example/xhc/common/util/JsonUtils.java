/*
 * Copyright (c) 2022 xiaohongchao.All Rights Reserved.
 */

package org.example.xhc.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsParser;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsSchema;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.example.xhc.common.constant.SystemConstants.CSV_DEFAULT_COLUMN_SEPARATOR;

/**
 * Json工具类
 *
 * @author xiaohongchao
 * @since 1.0.0
 */
@Slf4j
public final class JsonUtils {
    private static ObjectMapper mapper;
    private static YAMLMapper yamlMapper;
    private static JavaPropsMapper propsMapper;
    private static CsvMapper csvMapper;
    private static XmlMapper xmlMapper;

    /**
     * 序列化级别，默认只序列化属性值发生过改变的字段
     */
    private static JsonInclude.Include defaultPropertyInclusion = JsonInclude.Include.NON_NULL;

    /**
     * 是否缩进JSON格式
     */
    private static boolean isEnableIndentOutput = false;

    static {
        //初始化
        initMapper();
        //配置序列化级别
        configPropertyInclusion();
        //配置JSON缩进支持
        configIndentOutput();
        //配置普通属性
        configCommon();
        //配置特殊属性
        configSpecial();
    }

    /**
     * 私有化工具类构造函数
     */
    private JsonUtils() {
    }

    private static void initMapper() {
        mapper = new ObjectMapper();
        yamlMapper = new YAMLMapper();
        propsMapper = new JavaPropsMapper();
        csvMapper = new CsvMapper();
        xmlMapper = new XmlMapper();
    }

    private static void configCommon() {
        config(mapper);
        config(yamlMapper);
        config(propsMapper);
        config(csvMapper);
        config(xmlMapper);
    }

    private static void configPropertyInclusion() {
        mapper.setSerializationInclusion(defaultPropertyInclusion);
        yamlMapper.setSerializationInclusion(defaultPropertyInclusion);
        propsMapper.setSerializationInclusion(defaultPropertyInclusion);
        csvMapper.setSerializationInclusion(defaultPropertyInclusion);
        xmlMapper.setSerializationInclusion(defaultPropertyInclusion);
    }

    private static void configIndentOutput() {
        mapper.configure(SerializationFeature.INDENT_OUTPUT, isEnableIndentOutput);
        yamlMapper.configure(SerializationFeature.INDENT_OUTPUT, isEnableIndentOutput);
        propsMapper.configure(SerializationFeature.INDENT_OUTPUT, isEnableIndentOutput);
        csvMapper.configure(SerializationFeature.INDENT_OUTPUT, isEnableIndentOutput);
        xmlMapper.configure(SerializationFeature.INDENT_OUTPUT, isEnableIndentOutput);
    }

    private static void configSpecial() {
        //使用系统换行符
        yamlMapper.enable(YAMLGenerator.Feature.USE_PLATFORM_LINE_BREAKS);
        //允许注释
        yamlMapper.enable(JsonParser.Feature.ALLOW_COMMENTS);
        yamlMapper.enable(JsonParser.Feature.ALLOW_YAML_COMMENTS);
        //允许注释
        propsMapper.enable(JavaPropsParser.Feature.ALLOW_COMMENTS);
        propsMapper.enable(JavaPropsParser.Feature.ALLOW_YAML_COMMENTS);
        //去掉头尾空格
        csvMapper.enable(CsvParser.Feature.TRIM_SPACES);
        //忽略空行
        csvMapper.enable(CsvParser.Feature.SKIP_EMPTY_LINES);
        csvMapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
    }

    private static void config(ObjectMapper objectMapper) {
        //序列化BigDecimal时之间输出原始数字还是科学计数, 默认false, 即是否以toPlainString()科学计数方式来输出
        objectMapper.disable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
        //允许将JSON空字符串强制转换为null对象值
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        //允许单个数值当做数组处理
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        //禁止重复键, 抛出异常
        objectMapper.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
        //禁止使用int代表Enum的order()來反序列化Enum, 抛出异常
        objectMapper.enable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS);
        //有属性不能映射的时候不报错
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        //使用null表示集合类型字段是时不抛异常
        objectMapper.disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
        //对象为空时不抛异常
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        //允许在JSON中使用c/c++风格注释
        objectMapper.enable(JsonParser.Feature.ALLOW_COMMENTS);
        //强制转义非ascii字符
        objectMapper.disable(JsonGenerator.Feature.ESCAPE_NON_ASCII);
        //允许未知字段
        objectMapper.enable(JsonGenerator.Feature.IGNORE_UNKNOWN);
        //在JSON中允许未引用的字段名
        objectMapper.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);
        //时间格式
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        //识别单引号
        objectMapper.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        //识别Java8时间
        objectMapper.registerModule(new ParameterNamesModule());
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());
        //识别Guava包的类
        objectMapper.registerModule(new GuavaModule());
    }

    /**
     * 设置序列化级别
     * NON_NULL：序列化非空的字段
     * NON_EMPTY：序列化非空字符串和非空的字段
     * NON_DEFAULT：序列化属性值发生过改变的字段
     */
    public static void setSerializationInclusion(JsonInclude.Include inclusion) {
        defaultPropertyInclusion = inclusion;
        configPropertyInclusion();
    }

    /**
     * 设置是否开启JSON格式美化
     *
     * @param isEnable 为true表示开启, 默认false, 有些场合为了便于排版阅读则需要对输出做缩放排列
     */
    public static void setIndentOutput(boolean isEnable) {
        isEnableIndentOutput = isEnable;
        configIndentOutput();
    }


    /**
     * JSON反序列化
     */
    public static <V> V from(URL url, Class<V> c) {
        try {
            return mapper.readValue(url, c);
        } catch (IOException e) {
            //log.error("jackson from error, url: {}, type: {}", url.getPath(), c, e);
            return null;
        }
    }

    /**
     * JSON反序列化
     */
    public static <V> V from(InputStream inputStream, Class<V> c) {
        try {
            return mapper.readValue(inputStream, c);
        } catch (IOException e) {
            //log.error("jackson from error, type: {}", c, e);
            return null;
        }
    }

    /**
     * JSON反序列化
     */
    public static <V> V from(File file, Class<V> c) {
        try {
            return mapper.readValue(file, c);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * JSON反序列化
     */
    public static <V> V from(Object jsonObj, Class<V> c) {
        try {
            return mapper.readValue(jsonObj.toString(), c);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * JSON反序列化
     */
    public static <V> V from(String json, Class<V> c) {
        try {
            return mapper.readValue(json, c);
        } catch (IOException e) {
            //log.error("jackson from error, json: {}, type: {}", json, c, e);
            return null;
        }
    }

    /**
     * JSON反序列化
     */
    public static <V> V from(URL url, TypeReference<V> type) {
        try {
            return mapper.readValue(url, type);
        } catch (IOException e) {
            //log.error("jackson from error, url: {}, type: {}", url.getPath(), type, e);
            return null;
        }
    }

    /**
     * JSON反序列化
     */
    public static <V> V from(InputStream inputStream, TypeReference<V> type) {
        try {
            return mapper.readValue(inputStream, type);
        } catch (IOException e) {
            //log.error("jackson from error, type: {}", type, e);
            return null;
        }
    }

    /**
     * JSON反序列化
     */
    public static <V> V from(File file, TypeReference<V> type) {
        try {
            return mapper.readValue(file, type);
        } catch (IOException e) {
            //log.error("jackson from error, file path: {}, type: {}", file.getPath(), type, e);
            return null;
        }
    }

    /**
     * JSON反序列化
     */
    public static <V> V from(Object jsonObj, TypeReference<V> type) {
        try {
            return mapper.readValue(jsonObj.toString(), type);
        } catch (IOException e) {
            //log.error("jackson from error, json: {}, type: {}", jsonObj.toString(), type, e);
            return null;
        }
    }

    /**
     * JSON反序列化
     */
    public static <V> V from(String json, TypeReference<V> type) {
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            //log.error("jackson from error, json: {}, type: {}", json, type, e);
            return null;
        }
    }

    /**
     * 反序列化Resources目录下的Yaml文件
     *
     * @param name 文件名
     */
    public static <V> V fromYamlResource(String name, Class<V> c) {
        try (InputStream inputStream = getResourceStream(name); InputStreamReader reader = getResourceReader(inputStream)) {
            if (reader == null) {
                return null;
            }
            return yamlMapper.readValue(reader, c);
        } catch (IOException e) {
            //log.error("jackson from yaml Resource error, name: {}, type: {}", name, c, e);
            return null;
        }
    }

    /**
     * 反序列化Resources目录下的Yaml文件
     *
     * @param name 文件名
     */
    public static <V> V fromYamlResource(String name, TypeReference<V> type) {
        try (InputStream inputStream = getResourceStream(name); InputStreamReader reader = getResourceReader(inputStream)) {
            if (reader == null) {
                return null;
            }
            return yamlMapper.readValue(reader, type);
        } catch (IOException e) {
            //log.error("jackson from yaml Resource error, name: {}, type: {}", name, type, e);
            return null;
        }
    }

    /**
     * 反序列化Yaml文件
     *
     * @param path 文件路径
     */
    public static <V> V fromYamlFile(String path, Class<V> c) {
        try {
            return yamlMapper.readValue(new File(path), c);
        } catch (IOException e) {
            //log.error("jackson from yaml error, path: {}, type: {}", path, c, e);
            return null;
        }
    }

    /**
     * 反序列化Yaml文件
     *
     * @param path 文件路径
     */
    public static <V> V fromYamlFile(String path, TypeReference<V> type) {
        try {
            return yamlMapper.readValue(new File(path), type);
        } catch (IOException e) {
            //log.error("jackson from yaml error, path: {}, type: {}", path, type, e);
            return null;
        }
    }

    /**
     * 反序列化Resources目录下的Properties文件
     *
     * @param name 文件名
     */
    public static <V> V fromPropResource(String name, Class<V> c) {
        try (InputStream inputStream = getResourceStream(name); InputStreamReader reader = getResourceReader(inputStream)) {
            if (reader == null) {
                return null;
            }
            return propsMapper.readValue(reader, c);
        } catch (IOException e) {
            //log.error("jackson from properties Resource error, name: {}, type: {}", name, c, e);
            return null;
        }
    }

    /**
     * 反序列化Resources目录下的Properties文件
     *
     * @param name 文件名
     */
    public static <V> V fromPropResource(String name, TypeReference<V> type) {
        try (InputStream inputStream = getResourceStream(name); InputStreamReader reader = getResourceReader(inputStream)) {
            if (reader == null) {
                return null;
            }
            return propsMapper.readValue(reader, type);
        } catch (IOException e) {
            //log.error("jackson from properties Resource error, name: {}, type: {}", name, type, e);
            return null;
        }
    }

    /**
     * 反序列化Resources目录下的Csv文件（受限于CSV的格式，Jackson不支持深层次结构的CSV反序列化，不支持嵌套类）
     *
     * @param name 文件名
     */
    public static <V> List<V> fromCsvResource(String name, Class<V> c) {
        return fromCsvResource(name, CSV_DEFAULT_COLUMN_SEPARATOR, c);
    }

    /**
     * 反序列化Resources目录下的Csv文件（受限于CSV的格式，Jackson不支持深层次结构的CSV反序列化，不支持嵌套类）
     *
     * @param name      文件名
     * @param separator cloumn的分隔符
     */
    public static <V> List<V> fromCsvResource(String name, String separator, Class<V> c) {
        try (InputStream inputStream = getResourceStream(name); InputStreamReader reader = getResourceReader(inputStream)) {
            if (reader == null) {
                return null;
            }
            CsvSchema schema = CsvSchema.builder().setColumnSeparator(separator.charAt(0)).setUseHeader(true).build();
            return (List<V>) csvMapper.reader(schema).forType(c).readValues(reader).readAll();
        } catch (IOException e) {
            //log.error("jackson from csv Resource error, name: {}, type: {}", name, c, e);
            return null;
        }
    }

    /**
     * 反序列化Csv文件（受限于CSV的格式，Jackson不支持深层次结构的CSV反序列化，不支持嵌套类）
     *
     * @param path 文件路径
     */
    public static <V> List<V> fromCsvFile(String path, Class<V> c) {
        return fromCsvFile(path, CSV_DEFAULT_COLUMN_SEPARATOR, c);
    }

    /**
     * 反序列化Csv文件（受限于CSV的格式，Jackson不支持深层次结构的CSV反序列化，不支持嵌套类）
     *
     * @param separator cloumn的分隔符
     * @param path      文件路径
     */
    public static <V> List<V> fromCsvFile(String path, String separator, Class<V> c) {
        try {
            CsvSchema schema = CsvSchema.builder().setColumnSeparator(separator.charAt(0)).setUseHeader(true).build();
            return (List<V>) csvMapper.reader(schema).forType(c).readValues(new File(path)).readAll();
        } catch (IOException e) {
            //log.error("jackson from csv error, path: {}, type: {}", path, c, e);
            return null;
        }
    }


    /**
     * 反序列化Resources目录下的Xml文件
     *
     * @param name 文件名
     */
    public static <V> V fromXmlResource(String name, Class<V> c) {
        try (InputStream inputStream = getResourceStream(name); InputStreamReader reader = getResourceReader(inputStream)) {
            if (reader == null) {
                return null;
            }
            return xmlMapper.readValue(reader, c);
        } catch (IOException e) {
            //log.error("jackson from xml Resource error, name: {}, type: {}", name, c, e);
            return null;
        }
    }

    /**
     * 反序列化Resources目录下的Xml文件
     *
     * @param name 文件名
     */
    public static <V> V fromXmlResource(String name, TypeReference<V> type) {
        try (InputStream inputStream = getResourceStream(name); InputStreamReader reader = getResourceReader(inputStream)) {
            if (reader == null) {
                return null;
            }
            return xmlMapper.readValue(reader, type);
        } catch (IOException e) {
            //log.error("jackson from xml Resource error, name: {}, type: {}", name, type, e);
            return null;
        }
    }

    /**
     * 反序列化Xml文件
     *
     * @param path 文件路径
     */
    public static <V> V fromXmlFile(String path, Class<V> c) {
        try {
            return xmlMapper.readValue(new File(path), c);
        } catch (IOException e) {
            //log.error("jackson from xml error, path: {}, type: {}", path, c, e);
            return null;
        }
    }

    /**
     * 反序列化Xml文件
     *
     * @param path 文件路径
     */
    public static <V> V fromXmlFile(String path, TypeReference<V> type) {
        try {
            return xmlMapper.readValue(new File(path), type);
        } catch (IOException e) {
            //log.error("jackson from xml error, path: {}, type: {}", path, type, e);
            return null;
        }
    }

    /**
     * 反序列化Xml字符串
     */
    public static <V> V fromXml(String xml, Class<V> c) {
        try {
            return xmlMapper.readValue(xml, c);
        } catch (IOException e) {
            //log.error("jackson from xml error, xml: {}, type: {}", xml, c, e);
            return null;
        }
    }

    /**
     * 反序列化Xml字符串
     */
    public static <V> V fromXml(String xml, TypeReference<V> type) {
        try {
            return xmlMapper.readValue(xml, type);
        } catch (IOException e) {
            //log.error("jackson from xml error, xml: {}, type: {}", xml, type, e);
            return null;
        }
    }

    /**
     * 序列化为JSON
     */
    public static <V> String to(List<V> list) {
        try {
            return mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            //log.error("jackson to error, obj: {}", list, e);
            return null;
        }
    }

    /**
     * 序列化为JSON
     */
    public static <V> String to(V v) {
        try {
            return mapper.writeValueAsString(v);
        } catch (JsonProcessingException e) {
            //log.error("jackson to error, obj: {}", v, e);
            return null;
        }
    }

    /**
     * 序列化为JSON
     */
    public static <V> void toFile(String path, List<V> list) {
        try (Writer writer = new FileWriter(new File(path), true)) {
            mapper.writer().writeValues(writer).writeAll(list);
            writer.flush();
        } catch (Exception e) {
            //log.error("jackson to file error, path: {}, list: {}", path, list, e);
        }
    }

    /**
     * 序列化为JSON
     */
    public static <V> void toFile(String path, V v) {
        try (Writer writer = new FileWriter(new File(path), true)) {
            mapper.writer().writeValues(writer).write(v);
            writer.flush();
        } catch (Exception e) {
            //log.error("jackson to file error, path: {}, obj: {}", path, v, e);
        }
    }

    /**
     * 序列化为YAML
     */
    public static <V> String toYaml(V v) {
        try {
            return yamlMapper.writeValueAsString(v);
        } catch (JsonProcessingException e) {
            //log.error("jackson to yaml error, obj: {}", v, e);
            return null;
        }
    }

    /**
     * 序列化为YAML文件
     */
    public static <V> void toYamlFile(String path, V v) {
        try (Writer writer = new FileWriter(new File(path), true)) {
            yamlMapper.writeValue(writer, v);
            writer.flush();
        } catch (Exception e) {
            //log.error("jackson to yaml file error, path: {}, obj: {}", path, v, e);
        }
    }

    /**
     * 序列化为Properties
     */
    public static <V> String toProp(V v) {
        try {
            String string = propsMapper.writeValueAsString(v);
            return new String(string.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            //log.error("jackson to properties error, obj: {}", v, e);
            return null;
        }
    }

    /**
     * 序列化为Properties文件
     */
    public static <V> void toPropFile(String path, V v) {
        try (Writer writer = new FileWriter(new File(path), true)) {
            JavaPropsSchema schema = JavaPropsSchema.emptySchema();
            propsMapper.writer(schema).writeValues(writer).write(v);
            writer.flush();
        } catch (Exception e) {
            //log.error("jackson to properties file error, path: {}, obj: {}", path, v, e);
        }
    }

    /**
     * 序列化为CSV
     */
    public static <V> String toCsv(List<V> list) {
        return toCsv(CSV_DEFAULT_COLUMN_SEPARATOR, list);
    }

    /**
     * 序列化为CSV
     */
    public static <V> String toCsv(String separator, List<V> list) {
        try {
            Class type = list.get(0).getClass();
            CsvSchema schema = csvMapper.schemaFor(type).withHeader().withColumnSeparator(separator.charAt(0));
            return csvMapper.writer(schema).writeValueAsString(list);
        } catch (JsonProcessingException e) {
            //log.error("jackson to csv error, obj: {}", list, e);
            return null;
        }
    }

    /**
     * 序列化为CSV
     */
    public static <V> String toCsv(V v) {
        return toCsv(CSV_DEFAULT_COLUMN_SEPARATOR, v);
    }

    /**
     * 序列化为CSV
     */
    public static <V> String toCsv(String separator, V v) {
        try {
            CsvSchema schema = csvMapper.schemaFor(v.getClass()).withHeader().withColumnSeparator(separator.charAt(0));
            return csvMapper.writer(schema).writeValueAsString(v);
        } catch (JsonProcessingException e) {
            //log.error("jackson to csv error, obj: {}", v, e);
            return null;
        }
    }

    /**
     * 序列化为CSV文件
     */
    public static <V> void toCsvFile(String path, List<V> list) {
        toCsvFile(path, CSV_DEFAULT_COLUMN_SEPARATOR, list);
    }

    /**
     * 序列化为CSV文件
     */
    public static <V> void toCsvFile(String path, String separator, List<V> list) {
        try (Writer writer = new FileWriter(new File(path), true)) {
            Class type = list.get(0).getClass();
            CsvSchema schema = csvMapper.schemaFor(type).withHeader().withColumnSeparator(separator.charAt(0));
            csvMapper.writer(schema).writeValues(writer).writeAll(list);
            writer.flush();
        } catch (Exception e) {
            //log.error("jackson to csv file error, path: {}, separator: {}, list: {}", path, separator, list, e);
        }
    }

    /**
     * 序列化为CSV文件
     */
    public static <V> void toCsvFile(String path, V v) {
        toCsvFile(path, CSV_DEFAULT_COLUMN_SEPARATOR, v);
    }

    /**
     * 序列化为CSV文件
     */
    public static <V> void toCsvFile(String path, String separator, V v) {
        try (Writer writer = new FileWriter(new File(path), true)) {
            CsvSchema schema = csvMapper.schemaFor(v.getClass()).withHeader().withColumnSeparator(separator.charAt(0));
            csvMapper.writer(schema).writeValues(writer).write(v);
            writer.flush();
        } catch (Exception e) {
            //log.error("jackson to csv file error, path: {}, separator: {}, obj: {}", path, separator, v, e);
        }
    }

    /**
     * 序列化为XML
     */
    public static <V> String toXml(V v) {
        return toXml(v, true);
    }

    /**
     * 序列化为XML
     */
    public static <V> String toXml(V v, boolean isIndent) {
        try {
            if (isIndent) {
                return xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(v);
            } else {
                return xmlMapper.writeValueAsString(v);
            }
        } catch (JsonProcessingException e) {
            //log.error("jackson to xml error, obj: {}, isIndent, {}", v, isIndent, e);
            return null;
        }
    }

    /**
     * 序列化为XML文件
     */
    public static <V> void toXmlFile(String path, V v) {
        toXmlFile(path, v, true);
    }

    /**
     * 序列化为XML文件
     */
    public static <V> void toXmlFile(String path, V v, boolean isIndent) {
        try (Writer writer = new FileWriter(new File(path), true)) {
            if (isIndent) {
                xmlMapper.writerWithDefaultPrettyPrinter().writeValue(writer, v);
            } else {
                xmlMapper.writeValue(writer, v);
            }
            writer.flush();
        } catch (Exception e) {
            //log.error("jackson to xml file error, path: {}, obj: {}, isIndent: {}", path, v, isIndent, e);
        }
    }

    /**
     * 从json串中获取某个字段
     *
     * @return String
     */
    public static String getString(String json, String key) {
        if (ObjectUtils.isEmpty(json)) {
            return null;
        }
        try {
            JsonNode node = mapper.readTree(json);
            if (null != node) {
                return node.get(key).toString();
            } else {
                return null;
            }
        } catch (IOException e) {
            //log.error("jackson get string error, json: {}, pullWaybillKey: {}", json, key, e);
            return null;
        }
    }

    /**
     * 从json串中获取某个字段
     *
     * @return int
     */
    public static Integer getInt(String json, String key) {
        if (ObjectUtils.isEmpty(json)) {
            return null;
        }
        try {
            JsonNode node = mapper.readTree(json);
            if (null != node) {
                return node.get(key).intValue();
            } else {
                return null;
            }
        } catch (IOException e) {
            //log.error("jackson get int error, json: {}, pullWaybillKey: {}", json, key, e);
            return null;
        }
    }

    /**
     * 从json串中获取某个字段
     *
     * @return long
     */
    public static Long getLong(String json, String key) {
        if (ObjectUtils.isEmpty(json)) {
            return null;
        }
        try {
            JsonNode node = mapper.readTree(json);
            if (null != node) {
                return node.get(key).longValue();
            } else {
                return null;
            }
        } catch (IOException e) {
            //log.error("jackson get long error, json: {}, pullWaybillKey: {}", json, key, e);
            return null;
        }
    }

    /**
     * 从json串中获取某个字段
     *
     * @return double
     */
    public static Double getDouble(String json, String key) {
        if (ObjectUtils.isEmpty(json)) {
            return null;
        }
        try {
            JsonNode node = mapper.readTree(json);
            if (null != node) {
                return node.get(key).doubleValue();
            } else {
                return null;
            }
        } catch (IOException e) {
            //log.error("jackson get double error, json: {}, pullWaybillKey: {}", json, key, e);
            return null;
        }
    }

    /**
     * 从json串中获取某个字段
     *
     * @return double
     */
    public static BigInteger getBigInteger(String json, String key) {
        if (ObjectUtils.isEmpty(json)) {
            return new BigInteger(String.valueOf(0.00));
        }
        try {
            JsonNode node = mapper.readTree(json);
            if (null != node) {
                return node.get(key).bigIntegerValue();
            } else {
                return null;
            }
        } catch (IOException e) {
            //log.error("jackson get biginteger error, json: {}, pullWaybillKey: {}", json, key, e);
            return null;
        }
    }

    /**
     * 从json串中获取某个字段
     *
     * @return double
     */
    public static BigDecimal getBigDecimal(String json, String key) {
        if (ObjectUtils.isEmpty(json)) {
            return null;
        }
        try {
            JsonNode node = mapper.readTree(json);
            if (null != node) {
                return node.get(key).decimalValue();
            } else {
                return null;
            }
        } catch (IOException e) {
            //log.error("jackson get bigdecimal error, json: {}, pullWaybillKey: {}", json, key, e);
            return null;
        }
    }

    /**
     * 从json串中获取某个字段
     *
     * @return boolean, 默认为false
     */
    public static boolean getBoolean(String json, String key) {
        if (ObjectUtils.isEmpty(json)) {
            return false;
        }
        try {
            JsonNode node = mapper.readTree(json);
            if (null != node) {
                return node.get(key).booleanValue();
            } else {
                return false;
            }
        } catch (IOException e) {
            //log.error("jackson get boolean error, json: {}, pullWaybillKey: {}", json, key, e);
            return false;
        }
    }

    /**
     * 从json串中获取某个字段
     *
     * @return boolean, 默认为false
     */
    public static byte[] getByte(String json, String key) {
        if (ObjectUtils.isEmpty(json)) {
            return null;
        }
        try {
            JsonNode node = mapper.readTree(json);
            if (null != node) {
                return node.get(key).binaryValue();
            } else {
                return null;
            }
        } catch (IOException e) {
            //log.error("jackson get byte error, json: {}, pullWaybillKey: {}", json, key, e);
            return null;
        }
    }

    /**
     * 从json串中获取某个字段
     *
     * @return boolean, 默认为false
     */
    public static <T> List<T> getList(String json, String key) {
        if (ObjectUtils.isEmpty(json)) {
            return Collections.emptyList();
        }

        String string = getString(json, key);
        return from(string, new TypeReference<ArrayList<T>>() {
        });
    }

    /**
     * 向json中添加属性
     *
     * @return json
     */
    public static <T> String add(String json, String key, T value) {
        try {
            JsonNode node = mapper.readTree(json);
            add(node, key, value);
            return node.toString();
        } catch (IOException e) {
            // log.error("jackson add error, json: {}, pullWaybillKey: {}, value: {}", json, key, value, e);
            return json;
        }
    }

    /**
     * 向json中添加属性
     */
    private static <T> void add(JsonNode jsonNode, String key, T value) {
        if (value instanceof String) {
            ((ObjectNode) jsonNode).put(key, (String) value);
        } else if (value instanceof Short) {
            ((ObjectNode) jsonNode).put(key, (Short) value);
        } else if (value instanceof Integer) {
            ((ObjectNode) jsonNode).put(key, (Integer) value);
        } else if (value instanceof Long) {
            ((ObjectNode) jsonNode).put(key, (Long) value);
        } else if (value instanceof Float) {
            ((ObjectNode) jsonNode).put(key, (Float) value);
        } else if (value instanceof Double) {
            ((ObjectNode) jsonNode).put(key, (Double) value);
        } else if (value instanceof BigDecimal) {
            ((ObjectNode) jsonNode).put(key, (BigDecimal) value);
        } else if (value instanceof BigInteger) {
            ((ObjectNode) jsonNode).put(key, (BigInteger) value);
        } else if (value instanceof Boolean) {
            ((ObjectNode) jsonNode).put(key, (Boolean) value);
        } else if (value instanceof byte[]) {
            ((ObjectNode) jsonNode).put(key, (byte[]) value);
        } else {
            ((ObjectNode) jsonNode).put(key, to(value));
        }
    }

    /**
     * 除去json中的某个属性
     *
     * @return json
     */
    public static String remove(String json, String key) {
        try {
            JsonNode node = mapper.readTree(json);
            ((ObjectNode) node).remove(key);
            return node.toString();
        } catch (IOException e) {
            //log.error("jackson remove error, json: {}, pullWaybillKey: {}", json, key, e);
            return json;
        }
    }

    /**
     * 修改json中的属性
     */
    public static <T> String update(String json, String key, T value) {
        try {
            JsonNode node = mapper.readTree(json);
            ((ObjectNode) node).remove(key);
            add(node, key, value);
            return node.toString();
        } catch (IOException e) {
            //log.error("jackson update error, json: {}, pullWaybillKey: {}, value: {}", json, key, value, e);
            return json;
        }
    }

    /**
     * 格式化Json(美化)
     *
     * @return json
     */
    public static String format(String json) {
        try {
            JsonNode node = mapper.readTree(json);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        } catch (IOException e) {
            //log.error("jackson format json error, json: {}", json, e);
            return json;
        }
    }

    /**
     * 判断字符串是否是json
     *
     * @return json
     */
    public static boolean isJson(String json) {
        try {
            mapper.readTree(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static InputStream getResourceStream(String name) {
        return JsonUtils.class.getClassLoader().getResourceAsStream(name);
    }

    private static InputStreamReader getResourceReader(InputStream inputStream) {
        if (null == inputStream) {
            return null;
        }
        return new InputStreamReader(inputStream, StandardCharsets.UTF_8);
    }
}
