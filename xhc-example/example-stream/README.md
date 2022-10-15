# Java8函数式编程心得

## 简介

整理的一些java stream范例

## 组织结构

``` lua
example-stream
├── xhc-parent -- 统一的Maven父类，统一管理第三方软件版本
├── xhc-common -- 通用代码
├── xhc-demo -- 演示项目
│   ├── demo-parent -- Maven父类
│   ├── demo-provider -- 服务提供者
└   └── demo-consumer -- 服务消费者
```

## 技术理解

### 集合操作

集合操作的语法糖集合

#### Stream

> 常用范例

- `合并` 
  ``` Java
  Stream.of(asList(1, 2, 3), asList(3, 4)).flatMap(Collection::stream).collect(toList())
  
  结果： asList(1, 2, 3, 3, 4)
  ```
- `分组` 分组：File -> Settings -> Editor -> Copyright
- `挑选`
- `排序` 设置入口：File -> Settings -> Editor -> Copyright
- `并集` 分组：File -> Settings -> Editor -> Copyright
- `交集` 设置入口：File -> Settings -> Editor -> Copyright
- `并集` 设置入口：File -> Settings -> Editor -> Copyright

#### 集合类基本操作

> 常用技巧

- `新建` 设置入口：File -> Settings -> Editor -> Copyright
- `添加` 设置入口：File -> Settings -> Editor -> Copyright
- `删减` 设置入口：File -> Settings -> Editor -> Copyright
- `销毁` 设置入口：File -> Settings -> Editor -> Copyright

### FunctionInterface

对齐C++的函数指针？（方法引用）

> 常用技巧

- 详细设置请参考：[官方文档](https://www.jetbrains.com/help/idea/2021.3/configuring-project-and-ide-settings.html)
- `定义文件头的注释内容` 设置入口：File -> Settings -> Editor -> File and Code Templates -> Includes:File header
