# 代码范例

## 简介

整理一些常用操作的编码最优解

## 集合操作

Java8函数式编程实现为主

### 单集合操作

#### 排序

- `sorted`
  ``` Java
  Stream.of("a2", "abc", "a")
        .sorted(Comparator.naturalOrder())
        .collect(toList())
  ```

  ``` Java
  ("a", "a2", "abc")
  ```

- `multi sorted`
  ``` Java
  students.stream()
          .sorted(Comparator
                  .comparing(Student::getAge, Comparator.naturalOrder())
                  .thenComparing(Student::getBirthday, Comparator.reverseOrder())
          )
          .collect(toList())
  ```

  ``` Java
    - no: "20200107"
      name: "Eva"
      sex: "FEMALE"
      age: 7
      birthday: "2013-12-09T05:24:20"
      classNo: "202001"
    - no: "20200215"
      name: "tom"
      sex: "MALE"
      age: 7
      birthday: "2013-01-29T15:05:41"
      classNo: "202002"
    - no: "20190321"
      name: "jack"
      sex: "MALE"
      age: 8
      birthday: "2012-10-19T15:05:41"
      classNo: "201903"
  ```

  >  技术理解
  Comparator支持嵌套，即comparing、thenComparing中，对排序字段再次指定一个复杂Comparator

#### 查找

##### 定位

- `min、max`
  ``` Java
  Stream.of("a", "1abc", "abc1").filter(value -> isDigit(value.charAt(0))).collect(toList())

  结果: singletonList("1abc")
  ```

##### 过滤

- `filter`
  ``` Java
  Stream.of(asList(1, 2, 3), asList(3, 4)).flatMap(Collection::stream).collect(toList())

  结果: asList(1, 2, 3, 3, 4)
  ```

##### 分组

- `groupingBy`
  ``` Java
  Stream.of(asList(1, 2, 3), asList(3, 4)).flatMap(Collection::stream).collect(toList())

  结果: asList(1, 2, 3, 3, 4)
  ```

#### 循环

- `sum`
  ``` Java
  Stream.of("a", "1abc", "abc1").filter(value -> isDigit(value.charAt(0))).collect(toList())

  结果: singletonList("1abc")
  ```

### 多集合操作

#### 合集

- `union all`
  ``` Java
  Stream.of(asList(1, 2, 3), asList(3, 4))
        .flatMap(Collection::stream)
        .collect(toList());
  
  结果: asList(1, 2, 3, 3, 4)
  ```

#### 并集

- `union`
  ``` Java
  Stream.of(asList(1, 2, 3), asList(3, 4))
        .flatMap(Collection::stream)
        .distinct()
        .collect(toList());
  
  结果: asList(1, 2, 3, 4)
  ```

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
