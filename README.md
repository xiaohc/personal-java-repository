# 个人Java库

## 简介
个人Java库，用于快速搭建项目，内容主要是收集的范例，整理的日常经验。

## 组织结构

``` lua
personal-java-repository
├── commons -- 工具类及通用代码模块
├── commons-dependencies -- 通用模块三方件的依赖版本统一管理
├── demo -- 演示项目
│   ├── demo-consumer -- 消费者演示项目
├── demo-dependencies -- 演示项目二方件的依赖版本统一管理
└── demo-parent -- 演示项目统一的Maven父类
```

## 技术选型

### 后端技术

| 技术                   | 说明                 | 官网                                                 |
| ---------------------- | -------------------- | ---------------------------------------------------- |
| Spring Cloud           | 微服务框架           | https://spring.io/projects/spring-cloud              |
| Spring Cloud Alibaba   | 微服务框架           | https://github.com/alibaba/spring-cloud-alibaba      |
| Spring Boot            | 容器+MVC框架         | https://spring.io/projects/spring-boot               |
| Spring Security Oauth2 | 认证和授权框架       | https://spring.io/projects/spring-security-oauth     |
| MyBatis                | ORM框架              | http://www.mybatis.org/mybatis-3/zh/index.html       |
| MyBatisGenerator       | 数据层代码生成       | http://www.mybatis.org/generator/index.html          |
| PageHelper             | MyBatis物理分页插件  | http://git.oschina.net/free/Mybatis_PageHelper       |
| Knife4j                | 文档生产工具         | https://github.com/xiaoymin/swagger-bootstrap-ui     |
| Elasticsearch          | 搜索引擎             | https://github.com/elastic/elasticsearch             |
| RabbitMq               | 消息队列             | https://www.rabbitmq.com/                            |
| Redis                  | 分布式缓存           | https://redis.io/                                    |
| MongoDb                | NoSql数据库          | https://www.mongodb.com/                             |
| Docker                 | 应用容器引擎         | https://www.docker.com/                              |
| Druid                  | 数据库连接池         | https://github.com/alibaba/druid                     |
| OSS                    | 对象存储             | https://github.com/aliyun/aliyun-oss-java-sdk        |
| MinIO                  | 对象存储             | https://github.com/minio/minio                       |
| JWT                    | JWT登录支持          | https://github.com/jwtk/jjwt                         |
| LogStash               | 日志收集             | https://github.com/logstash/logstash-logback-encoder |
| Lombok                 | 简化对象封装工具     | https://github.com/rzwitserloot/lombok               |
| Seata                  | 全局事务管理框架     | https://github.com/seata/seata                       |
| Portainer              | 可视化Docker容器管理 | https://github.com/portainer/portainer               |
| Jenkins                | 自动化部署工具       | https://github.com/jenkinsci/jenkins                 |
| Kubernetes             | 应用容器管理平台     | https://kubernetes.io/                               |

### 前端技术

| 技术       | 说明                  | 官网                           |
| ---------- | --------------------- | ------------------------------ |
| Vue        | 前端框架              | https://vuejs.org/             |
| Vue-router | 路由框架              | https://router.vuejs.org/      |
| Vuex       | 全局状态管理框架      | https://vuex.vuejs.org/        |
| Element    | 前端UI框架            | https://element.eleme.io/      |
| Axios      | 前端HTTP框架          | https://github.com/axios/axios |
| v-charts   | 基于Echarts的图表框架 | https://v-charts.js.org/       |


## 环境搭建

### 开发工具

| 工具          | 版本号 | 下载                                                         |
| ------------- | ------ | ------------------------------------------------------------ |
| JDK           | 1.8    | https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html |
| Mysql         | 5.7    | https://www.mysql.com/                                       |
| Redis         | 7.0    | https://redis.io/download                                    |
| Elasticsearch | 7.17.3 | https://www.elastic.co/cn/downloads/elasticsearch            |
| Kibana        | 7.17.3 | https://www.elastic.co/cn/downloads/kibana                   |
| Logstash      | 7.17.3 | https://www.elastic.co/cn/downloads/logstash                 |
| MongoDb       | 5.0    | https://www.mongodb.com/download-center                      |
| RabbitMq      | 3.10.5 | http://www.rabbitmq.com/download.html                        |
| nginx         | 1.22   | http://nginx.org/en/download.html                            |

### 搭建步骤

#### IDEA

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

| 插件               |     介绍                                                                          |
| -------------------|---------------------------------------------------------------------------------- |
| IdeaVim            |     VIM操作方式支持插件                                                           |
