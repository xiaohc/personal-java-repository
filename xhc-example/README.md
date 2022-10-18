# 代码范例

## 简介

整理一些常用操作的编码最优解

## 集合操作 

### Java8函数式编程

#### Stream

> 常用功能

##### 合并

- `flatMap`
  ``` Java
  Stream.of(asList(1, 2, 3), asList(3, 4)).flatMap(Collection::stream).collect(toList())
  
  结果: asList(1, 2, 3, 3, 4)
  ```

##### 分组

- `分组`
  ``` Java
  Stream.of(asList(1, 2, 3), asList(3, 4)).flatMap(Collection::stream).collect(toList())

  结果: asList(1, 2, 3, 3, 4)
  ```

##### 挑选

- `filter、min、max`
  ``` Java
  Stream.of("a", "1abc", "abc1").filter(value -> isDigit(value.charAt(0))).collect(toList())

  结果: singletonList("1abc")
  ```

##### 排序

- `sorted` 
  ``` Java
  Stream.of("a2", "abc", "a").sorted(Comparator.naturalOrder()).collect(toList())

  结果: asList("a", "a2", "abc")
  ```

##### 并集

- `并集` 

##### 交集

- `交集` 

##### 并集

- `并集` 

##### 集合类基本操作

> 常用技巧

- `新建` 设置入口：File -> Settings -> Editor -> Copyright
- `添加` 设置入口：File -> Settings -> Editor -> Copyright
- `删减` 设置入口：File -> Settings -> Editor -> Copyright
- `销毁` 设置入口：File -> Settings -> Editor -> Copyright

###### 技术理解

### FunctionInterface

对齐C++的函数指针？（方法引用）

> 常用技巧

- 详细设置请参考：[官方文档](https://www.jetbrains.com/help/idea/2021.3/configuring-project-and-ide-settings.html)
- `定义文件头的注释内容` 设置入口：File -> Settings -> Editor -> File and Code Templates -> Includes:File header
