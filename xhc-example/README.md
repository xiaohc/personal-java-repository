# 代码范例

## 简介

整理一些常用操作的编码最优解

## 集合操作 

### Stream

Java8函数式编程

#### 合并

- `flatMap`
  ``` Java
  Stream.of(asList(1, 2, 3), asList(3, 4)).flatMap(Collection::stream).collect(toList())
  
  结果: asList(1, 2, 3, 3, 4)
  ```

#### 分组

- `分组`
  ``` Java
  Stream.of(asList(1, 2, 3), asList(3, 4)).flatMap(Collection::stream).collect(toList())

  结果: asList(1, 2, 3, 3, 4)
  ```

#### 挑选

- `filter、min、max`
  ``` Java
  Stream.of("a", "1abc", "abc1").filter(value -> isDigit(value.charAt(0))).collect(toList())

  结果: singletonList("1abc")
  ```

#### 排序

- `sorted` 
  ``` Java
  Stream.of("a2", "abc", "a").sorted(Comparator.naturalOrder()).collect(toList())

  结果: asList("a", "a2", "abc")
  ```

#### 并集

- `并集` 

#### 交集

- `交集` 

#### 新建

- `新建` 

#### 添加

- `添加`

#### 删减

- `删减`

#### 销毁

- `销毁`


### FunctionInterface

#### 技术理解

对齐C++的函数指针？（方法引用）
