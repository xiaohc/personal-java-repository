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

#### 操作接口

> Stream数据流式操作，通用三步流程总结如下：
>
> 1. 初始操作：产生流 - 将集合类对象，转化为流对象
> 2. 中间操作：处理流 - 对流的元素执行处理操作（惰性方法为主）
> 3. 终结操作：产生处理结果 - 按收集策略执行终止流并输出结果的操作

##### 1. 初始操作

> - `Collection.stream()`：将对应集合转化成一个数据流
> - `Arrays.stream(T... values)`: 用参数生成一个数据流
> - `Stream.of(T... values)`: 用参数生成一个数据流
> - `Stream.generate(Supplier s)`: 使用元素生成器生成一个无序的数据流(Long.MAX_VALUE，可使用limit限制数量)
> - `Stream.concat(Stream a, Stream b)`: 连接二个数据流为一个数据流
> - `Stream.iterate(T seed, UnaryOperator f) `: 利用种子和生成新元素函数，生成一个流
> - `Stream.empty()`: 返回一个空的顺序流

##### 2. 中间操作

无状态的
> - `Stream.peek(Consumer action)`: 从结果流中消耗元素时，执行此步骤

有状态的
> - `Stream.sorted(Comparator comparator)`: 排序处理
> - `Stream.filter(Predicate predicate)`: 过滤数据
> - `Stream.skip(long maxSize)`: 过滤数据，丢弃流的前n元素后，返回剩余元素
> - `Stream.distinct()`: 对流中元素去重
> - `Stream.map(Function mapper)`: 映射处理
> - `Stream.mapToInt(ToIntFunction mapper)`: 映射为基本类型
> - `Stream.mapToLong(ToLongFunction mapper)`: 映射为基本类型
> - `Stream.mapToDouble(ToDoubleFunction mapper)`: 映射为基本类型
> - `Stream.flatMap(Function mapper)`: 用原数据流中每一个元素为参，来生成数据流，最终将其合并为一个数据流
> - `Stream.flatMapToInt(Function mapper)`: 映射为基本类型流并合流
> - `Stream.flatMapToLong(Function mapper)`: 映射为基本类型流并合流
> - `Stream.flatMapToDouble(Function mapper)`: 映射为基本类型流并合流

     💖 mapToInt() 对比 map()，IntStream流元素为基本类型，相比装箱类型，存储不用装箱，处理不用拆箱，性能、内存上表现更优

短路，有状态的
> - `Stream.limit(long maxSize)`: 过滤数据，按长度进行截断

##### 3. 终结操作

> - `Stream.reduce(BinaryOperator accumulator)`: 压缩处理，对流上的元素执行归约，并返回归约后的值
> - `Stream.reduce(T identity,BinaryOperator accumulator)`: 压缩处理 ≒ min、max、count
> - `Stream.reduce(T identity,BiFunction accumulator, BinaryOperator combiner)`: 压缩处理 ≒ min、max、count
> - `Stream.collect(Collector collector)`: 可变归约操作
> - `Stream.collect(Supplier supplier,BiConsumer accumulator, BiConsumer combiner)`: 可变归约操作
> - `Stream.count()`: 返回流元素的总个数
> - `Stream.max(Comparator comparator)`: 返回流中的最大元素
> - `Stream.min(Comparator comparator)`: 返回流中的最小元素
> - `Stream.forEach(Consumer action)`: 对每个元素执行action操作
> - `Stream.forEachOrdered(Consumer action)`: 对每个元素执行action操作
> - `Stream.toArray(*)`: 返回一个全部流元素的数组
> - `IntStream.summaryStatistics()`: 汇总所有int流元素的各种摘要数据

短路操作
> - `Stream.findAny()`: 返回流中任意一个元素
> - `Stream.findFirst()`: 返回流中第一个元素
> - `Stream.anyMatch(Predicate predicate)`: 返回是否匹配，流中存在任意一个元素，匹配predicate条件
> - `Stream.allMatch(Predicate predicate)`: 返回是否匹配，流中所有元素，均匹配predicate条件
> - `Stream.noneMatch(Predicate predicate)`: 返回是否都不匹配，流中所有元素，均不匹配predicate条件

#### 典型参数

##### 函数接口

| 接口                   | 函数原型                | 说明                            | 示例                                                 |
| --------------------- | ---------------------- | ------------------------------ | ---------------------------------------------------- |
| Predicate\<T>          | boolean test(T t);     | 断言，给定参数返回true或false       | 判断对象是否为空，` Objects::nonNull `                  |
| Supplier\<T>           | T get();               | 提供者，无参执行操作，直接产生结果    |  工厂方法                                                 |
| Consumer\<T>           | void accept(T t);      | 消费者，给定1个参数执行操作，不产生结果 | 打印日志                                        |
| BiConsumer<T, U>       | void accept(T t, U u); | 消费者，给定2个参数执行操作，不产生结果 | 打印日志                                        |
| Function<T,R>          | R apply(T t);          | 功能函数，接受1个参数，并产生结果    | 获得对象的名字，` Student::getAge `                           |
| BiFunction<T, U, R>    | R apply(T t, U u);     | 功能函数，接受2个参数，并产生结果    |                                                 |
| UnaryOperator\<T>      | T apply(T t);          | 一元运算符，参数与结果类型相同的函数  | 逻辑非（ !）                                              |
| BinaryOperator\<T>     | T apply(T t1, T t2);   | 二元运算符，参数与结果类型相同的函数  | 求两个数的乘积（ *）                                          |

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
