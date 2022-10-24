# 代码范例

## 简介

整理一些常用操作的编码最优解

## Stream API 集合操作

### 技术理解

> 👉  Lambda 表达式是一个匿名方法，对齐C++函数体  
> 👉  FunctionInterface 用于定义 Lambda表达式的类型，其实体对象对齐C++的函数指针  

### 单集合

#### 排序

- `sorted`
  ``` java
  Stream.of("a2", "abc", "a")
        .sorted(Comparator.naturalOrder())
        .collect(toList());
  ```

  ``` java
  List ⤵️ 
  - "a"
  - "a2"
  - "abc"
  ```

- `multi sorted`
  ``` java
  students.stream()
          .sorted(Comparator
                  .comparing(Student::getAge, Comparator.naturalOrder())
                  .thenComparing(Student::getBirthday, Comparator.reverseOrder())
          )
          .collect(toList());
  ```
  > 💖 Comparator支持嵌套，即comparing、thenComparing中，如果选定字段为POJO类型时，可为其指定一个组合Comparator

  ``` java
    List ⤵️
    - no: "20200107"
      name: "eva"
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

#### 查找

- `min、max`
  ``` java
  Stream.of("a", "1abc", "abc1")
        .min(Comparator.naturalOrder())
        .get();
  ```
  ``` java
  String ➡️ "1abc"
  ```

#### 遍历

- `sum`
  ``` java
  Stream.of(1, 2, 3)
        .mapToInt(Integer::valueOf)
        .sum();
  ```
  ``` java
  int ➡️ 6
  ```
 
#### 过滤

- `filter`
  ``` java
  students.stream()
          .filter(this::isSevenYearOld)
          .collect(toList());
  
  private boolean isSevenYearOld(Student student) {
    return student != null 
            && Objects.equals(student.getAge(), 7);
  }
  ```
  ``` java
  List ⤵️
  - no: "20200107"
    name: "eva"
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
  ```

#### 分组

- `groupingBy`
  ``` java
  students.stream()
          .collect(groupingBy(Student::getSex, mapping(Student::getName, joining(",", "[", "]"))));
  ```
  ``` java
  Map ⤵️
  MALE: "[jack,tom]"
  FEMALE: "[eva]"
  ```

- `reduce`

### 多集合

#### 合并

- `union all`
  ``` java
  Stream.of(asList(1, 2, 3), asList(3, 4))
        .flatMap(Collection::stream)
        .collect(toList());
  ```
  ``` java
  Stream.concat(Stream.of(1, 2, 3), Stream.of(3, 4))
        .collect(toList());
  ```  
  ``` java
  List ⤵️
  - 1
  - 2
  - 3
  - 3
  - 4
  ```

#### 并集

- `union`
  ``` java
  Stream.of(asList(1, 2, 3), asList(3, 4))
        .flatMap(Collection::stream)
        .distinct()
        .collect(toList());
  ```
  ``` java
  List ⤵️
  - 1
  - 2
  - 3
  - 4
  ```

#### 交集

- `交集`
  ``` java
  Stream.of(1, 2, 3)
          .filter(v -> asList(3, 4).contains(v))
          .collect(toList());
  ```
  ``` java
  List ⤵️
  - 3
  ```
