# Stream总结

## 简介

整理一些个人理解，及常用操作的编码最优解

## Java8函数式编程

### 技术理解

  ``` java
  BinaryOperator<Integer> add = (x, y) -> x + y;
  ```

> 👉  <kbd>Lambda Expression</kbd> ` (x, y) -> x + y ` 是一个匿名方法，对齐C++函数体  
> 👉  <kbd>FunctionInterface</kbd> ` BinaryOperator<T> ` 用于定义 Lambda表达式的类型，其实体对象 ` add ` 对齐C++的函数指针

应用1：流式处理语法糖，可提升代码可读性。如下：

> 如果某函数的返回，有一个固定的处理流程，可以考虑返回一个函数接口类型。

  ``` java
    @FunctionalInterface
    public interface IfHandle {
        void thenThrow(ErrorContext error);
    }
  ```

  ``` java
    public static IfHandle isNull(final Object object) {
        return error -> {
            if (object == null) {
                throw new BusinessException(error);
            }
        };
    }
  ```

  ``` java
    If.isNull(txnRequestDTO).thenThrow(TXN_REQUEST_ERROR);
    If.isNull(txnRequestDTO).or(isNull(txnRequestDTO.getTxnNo())).thenThrow(TXN_REQUEST_ERROR);
  ```

  ``` java
    If.haveError(txnRequestDTO).thenThrow(TXN_REQUEST_ERROR);  // JSR-303 bean validation
  ```

## Stream API

### 常用场景

> 查找唱片集中，所有歌曲长度大于60的歌曲名（要求歌曲名排序、不重复）

  ``` java
  albums.stream()
        .flatMap(album -> album.getTracks())
        .filter(track -> track.getLength() > 60)
        .sorted(Track::getName, Comparator.naturalOrder())
        .map(track -> track.getName())
        .collect(toSet());
  ```

### 使用经验

#### 通用三步曲

> Stream数据流式操作，通用流程总结如下：  
> 
> 1. 产生
> 2. 处理
> 3. 收集

####  1. 产生API

> - `Collection.stream()`：将对应集合转化成一个数据流
> - `Arrays.stream(T... values)`: 用参数生成一个数据流
> - `Stream.of(T... values)`: 用参数生成一个数据流
> - `Stream.generate(Supplier s)`: 使用元素生成器生成一个无序的数据流(Long.MAX_VALUE，可使用limit限制数量)
> - `Stream.concat(Stream a, Stream b)`: 连接二个数据流为一个数据流
> - `Stream.flatMap(Function mapper)`: 用原数据流中每一个元素为参，来生成数据流，最终将其合并为一个数据流
 
####  2. 处理API

> - filter 过滤数据
> - sorted 排序处理
> - map 映射处理

####  3. 收集API

> - collect
> - reduce 压缩处理 ≒ min、max
 
### 代码示例

#### `Stream.of()`

- ` Stream.of(T... values) ≒ Arrays.stream(values) `
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

#### `Stream.flatMap()`

用原数据流中每一个元素为参，来生成数据流，最终将其合并为一个数据流  

params:
> ` mapper - Function<T, Stream> `  
e.g.  
> ` mapper = (t) -> Stream.of(t) `  
> ` mapper = (t) -> t.getList() `

example:  
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

### 确定数据流

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

### 多集合操作

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
