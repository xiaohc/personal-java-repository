# 个人代码库/工具链

## 📜简介

个人Java库，用于以后快速搭建项目，内容主要是收集的范例，整理的日常经验。

## 📒组织结构

``` shell
personal-java-repository
├── xhc-parent   -- 统一的Maven父类，统一管理第三方软件版本
├── xhc-common   -- 通用代码
├── xhc-example  -- 范例代码
│   ├── example-mybatis-generator    -- 自动生成 Mybatis 实体、DAO和XML演示范例
│   ├── example-stream               -- Stream演示范例
├── xhc-demo     -- 演示项目
│   ├── demo-parent    -- DEMO服务的统一Maven父类
│   ├── demo-base      -- DEMO服务的基础件（使用FP思想，封装Result类型，处理数据转化、校验、错误和异常）
│   ├── demo-provider  -- 服务提供者
└   └── demo-consumer  -- 服务消费者
```

## 🖥️技术选型

### 后端技术

| 名称                   | 说明                 | 版本 | 参考                                                 |
| ---------------------- | -------------------- | ------ | ---------------------------------------------------- |
| Spring Boot            | 容器+MVC框架           | 2.6.12 | [官网](https://spring.io/projects/spring-boot)              |
| Spring Cloud           | 微服务框架             | 2021.0.4 | [官网](https://spring.io/projects/spring-cloud)              |
| Spring Cloud Alibaba   | 微服务框架             | 2021.0.4.0 | [官网](https://github.com/alibaba/spring-cloud-alibaba)     |
| Spring Security Oauth2 | 认证和授权框架          |        | [官网](https://spring.io/projects/spring-security-oauth)    |
| MyBatis                | ORM框架               | 3.5.2  | [官网](https://mybatis.org/mybatis-3/zh/index.html)      |
| MyBatisGenerator       | 数据层代码生成          | 1.4.1  | [官网](https://mybatis.org/generator/)         |
| PageHelper             | MyBatis物理分页插件     |        | [官网](http://git.oschina.net/free/Mybatis_PageHelper)      |
| Swagger                | 文档生产工具            | 3.0.0 |      |
| Druid                  | 数据库连接池            |        | [官网](https://github.com/alibaba/druid)                    |
| JWT                    | JWT登录支持            |        | [官网](https://github.com/jwtk/jjwt)                       |
| Lombok                 | 简化对象封装工具        | 1.18.24 | [官网](https://github.com/rzwitserloot/lombok)              |
| Seata                  | 全局事务管理框架        |        | [官网](https://github.com/seata/seata)                      |
| JDK                    | Java开发包            | 1.8    | [下载地址](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) |

- `Spring Cloud`和`Spring Boot`之间的依赖关系：[JSON文档](https://start.spring.io/actuator/info)  
- 金融级系统不建议使用`Hutool`（非千锤百炼代码，部分实现不够严谨），供参考。

#### 数据中间件

| 名称                   | 说明                  | 版本 | 参考                                                 |
| ---------------------- | --------------------- | ------ | ---------------------------------------------------- |
| Redis                  | 分布式缓存             | 7.0     | [官网](https://redis.io/)                                    |
| Kafka                  | 消息队列               |      |                             |
| RabbitMq               | 消息队列               | 3.10.5 | [官网](http://www.rabbitmq.com/download.html)                        |
| MongoDb                | NoSql 数据库           |  5.0   | [官网](https://www.mongodb.com/)                             |
| Mysql                  | 关系数据库              | 5.7    | [官网](https://www.mysql.com/)                                      |
| OSS                    | 对象存储                |       | [网址](https://github.com/aliyun/aliyun-oss-java-sdk)        |
| MinIO                  | 对象存储                |      | [网址](https://github.com/minio/minio)                       |

#### 基础软件

| 名称                   | 说明                  | 版本 | 参考                                                 |
| ---------------------- | --------------------- | ------ | ---------------------------------------------------- |
| Jenkins                | 自动化部署工具          |        | [网址](https://github.com/jenkinsci/jenkins)                 |
| Kubernetes             | 应用容器管理平台         |        | [官网](https://kubernetes.io/)                                |
| Docker                 | 应用容器引擎            |        | [官网](https://www.docker.com/)                               |
| nginx                  |                       | 1.22   | [官网](http://nginx.org/en/download.html)                             |
| Elasticsearch          | 搜索引擎               | 7.17.3 | [网址](https://github.com/elastic/elasticsearch)              |
| Kibana                 |                       |  | [官网](ttps://www.elastic.co/cn/downloads/kibana)                    |
| Logstash               |                       | | [官网](https://www.elastic.co/cn/downloads/logstash)                  |
| Portainer              | 可视化Docker容器管理     |        | [网址](https://github.com/portainer/portainer)                |
| LogStash               | 日志收集                |        | [网址](https://github.com/logstash/logstash-logback-encoder)  |

### 前端技术

| 名称       | 说明                   | 参考                           |
| ---------- | --------------------- | ------------------------------ |
| Vue        | 前端框架                | [官网](https://vuejs.org/)              |
| Vue-router | 路由框架                | [官网](https://router.vuejs.org/)       |
| Vuex       | 全局状态管理框架         | [官网](https://vuex.vuejs.org/)         |
| Element    | 前端UI框架              | [官网](https://element.eleme.io/)       |
| Axios      | 前端HTTP框架            | [网址](https://github.com/axios/axios)  |
| v-charts   | 基于Echarts的图表框架    | [官网](https://v-charts.js.org/)        |

## 🛠️开发工具

### 在线工具

| 名称        | 说明                  | 参考                           |
| ---------- | --------------------- | ------------------------------ |
| bejson     | JSON格式化             | [网址](https://www.bejson.com/explore/index_new/)              |

### IDEA

> 常用设置

- 详细设置请参考：[官方文档](https://www.jetbrains.com/help/idea/2021.3/configuring-project-and-ide-settings.html)
- `定义CopyRight内容` 设置入口：File -> Settings -> Editor -> Copyright
- `定义文件头的注释内容` 设置入口：File -> Settings -> Editor -> File and Code Templates -> Includes:File header
- `设置新建文件的换行符类型` 设置入口：File -> Settings -> Editor -> Code Style -> General:Line separator
- `设置新建文件的编码类型` 设置入口：File -> Settings -> Editor -> File Encodings -> *:"UTF-8"
- `显示代码间的空白字符` 设置入口：File -> Settings -> Editor -> General -> Appearance -> Show whitespaces
- `文件保存时自动整理imports` 设置入口：File -> Settings -> Editor -> General -> Auto Import
- `Alt+Enter自动添加serialVersionUID` 设置入口：File -> Settings -> Editor -> Inspections -> Find:"serialVersionUID"
- `实现快捷键增加注释文档` 设置入口：File -> Settings -> Keymap -> Find:"Fix doc comment"

> 常用插件

| 插件                  |   分类    |          介绍                                                            |
| ----------------------|-----------|------------------------------------------------------------------------- |
| IdeaVim               | 编码效率  |   使IDEA支持VIM操作方式                                                  |
| Lombok                | 编码效率  |   Java代码增强库                                                         |
| GenerateAllSetter     | 编码效率  |   一键生成所有setter方法                                                 |
| GsonFormatPlus        | 编码效率  |   一键根据json文本生成java类                                             |
| Java bean to json     | 编码效率  |   一键根据java类生成json文本                                             |
| MyBatisX              | 编码效率  |   数据库操作增强（自动生成代码有依赖三方，金融级系统不建议使用其自动生成的代码）                                                         |
| EasyCode-MybatisCodeHelper  | 编码效率  |   自动生成entity、DAO、Mapper文件（无侵入）                                                         |
| CamelCase             | 编码效率  |   代码的大小写切换                                                       |
| String Manipulation   | 编码效率  |   字符串操作增强                                                         |
| Diffblue cover        | 编码效率  |   自动生成单元测试                                                       |
| Arthas idea           | 调试增强  |   程序在线调试命令的生成工具                                             |
| Json Helper           | 调试增强  |   快速格式化 Json 文本                                                   |
| Restful Fast Request  | 调试增强  |   idea版Postman                                                          |
| DubboTest             | 调试增强  |   dubbo接口测试                                                          |
| RestfulToolkit        | 调试增强  |   根据url部分参数，快速搜索Controller方法                                |
| SonarLint             | 代码质量  |   静态代码检查                                                           |
| SpotBugs              | 代码质量  |   静态代码检查                                                           |
| Alibaba Java Coding G.| 代码质量  |   Java代码规范                                                           |
| CheckStyle            | 代码质量  |   代码格式检查                                                           |
| Maven Helper          | 源码阅读  |   高效分析POM的依赖关系                                                  |
| Translation           | 源码阅读  |   内置翻译                                                               |
| Statistic             | 源码阅读  |   统计代码行数                                                           |
| CodeGlance Pro        | 源码阅读  |   右侧显示代码轮廓                                                       |
| Rainbow Brackets      | 源码阅读  |   代码中特殊颜色加强括号，强调配对关系                                   |
| Grep Console          | 源码阅读  |   Console日志过滤及上色                                                  |
| SequenceDiagram       | 源码阅读  |   生成时序图，及导航到代码                                               |
| IDE Eval Reset        | 其他插件  |   延迟试用期                                                             |

> 常用技巧

- `改参，但不修改配置文件进行调试` 方法：使用程序启动参数，e.g. -Dspring.datasource.url=jdbc:mysql ... ...

## ⛪环境搭建

## 📐代码范例
> 整理一些常用操作的编码最优解

点击下面链接，进入代码范例仓库:

- [Stream API 集合操作](https://gitee.com/x-hc/personal-java-repository/tree/master/xhc-example/example-stream)