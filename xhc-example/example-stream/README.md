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
    If.haveError(txnRequestDTO).thenThrow(TXN_REQUEST_ERROR);  // JSR-303 bean validation
  ```

  ``` java
    If.isNull(txnRequestDTO).or(haveError(txnRequestDTO)).thenThrow(TXN_REQUEST_ERROR);
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
> 1. 产生：将集合类对象，转化为流对象
> 2. 处理：对流的元素执行处理操作（惰性方法为主）
> 3. 收集：按收集策略执行终端操作

#### 1. 流产生API

> - `Collection.stream()`：将对应集合转化成一个数据流
> - `Arrays.stream(T... values)`: 用参数生成一个数据流
> - `Stream.of(T... values)`: 用参数生成一个数据流
> - `Stream.generate(Supplier s)`: 使用元素生成器生成一个无序的数据流(Long.MAX_VALUE，可使用limit限制数量)
> - `Stream.concat(Stream a, Stream b)`: 连接二个数据流为一个数据流
> - `Stream.flatMap(Function mapper)`: 用原数据流中每一个元素为参，来生成数据流，最终将其合并为一个数据流

#### 2. 处理API

> - `Stream.sorted()`: 排序处理
> - `Stream.filter()`: 过滤数据
> - `Stream.map()`: 映射处理

#### 3. 收集API

> - `Stream.reduce()`: 压缩处理 ≒ min、max、count
> - `Stream.collect()`: 可变归约操作

### 代码示例

#### ✍️ Stream.of()

用参数生成一个数据流

simplify:
> Stream.of(T... values) ≒ Arrays.stream(values)

example:

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

#### ✍️ flatMap

用原数据流中每一个元素为参，来生成数据流，最终将其合并为一个数据流

simplify:
> <b>flatMap(Function<T, Stream> mapper)</b>  
> flatMapToInt(Function<T, IntStream> mapper)>  
> flatMapToLong(Function<T, LongStream> mapper)   
> flatMapToDouble(Function<T, DoubleStream> mapper)

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
  List ⤵️
  - 1
  - 2
  - 3
  - 3
  - 4
  ```

#### ✍️ sorted

对流元素进行排序

simplify:
> sorted()  
> <b>sorted(Comparator<T> comparator)</b>

e.g.
> ` comparator = (t1, t2) -> Integer.valueOf(t1) - Integer.valueOf(t2) `

##### <b>comparator</b>

> 💖 Comparator支持嵌套，即comparing、thenComparing中，如果选定字段为POJO类型时，可为其指定一个组合Comparator

example:

  ``` java
  students.stream()
          .sorted(Comparator
                  .comparing(Student::getAge, Comparator.naturalOrder())
                  .thenComparing(Student::getBirthday, Comparator.reverseOrder())
          )
          .collect(toList());
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
    - no: "20190321"
      name: "jack"
      sex: "MALE"
      age: 8
      birthday: "2012-10-19T15:05:41"
      classNo: "201903"
  ```

#### ✍️ filter

过滤数据，返回满足 predicate 条件的数据

simplify:
> filter(Predicate<T> predicate)

e.g.
> ` predicate = (t) -> t != null `
> ` predicate = (t) -> t > 7 `

example:

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

#### ✍️ collect

过滤数据，返回满足 predicate 条件的数据

simplify:
> collect(Collector<T, A, R> collector)  
> collect(Supplier<R> supplier, BiConsumer<R, T> accumulator, BiConsumer<R, R> combiner)

e.g.
> ` predicate = (t) -> t != null `
> ` predicate = (t) -> t > 7 `

example:

  ``` java
  students.stream()
          .collect(groupingBy(Student::getSex, mapping(Student::getName, joining(",", "[", "]"))));
  ```

  ``` java
  Map ⤵️
  MALE: "[jack,tom]"
  FEMALE: "[eva]"
  ```
