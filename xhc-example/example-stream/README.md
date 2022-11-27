# Stream总结

## 简介

整理一些个人理解，及常用操作的编码最优解

## Java8函数式编程

### 技术理解

  技术的首要使命是管理软件的复杂度，对比 <kbd>命令式编程</kbd> 和 <kbd>函数式编程</kbd>，显然函数式编程在求解复杂问题时更有优势

> 👉  <kbd>命令式编程</kbd>就像小学方法解数学题，正向思维一步一步算。  
> 👉  <kbd>函数式编程</kbd>就像使用方程式解数学题，使用 y=ax+b 这种函数，用抽象来解决很复杂的问题。  

  p.s.  管理软件复杂度的2种方向：模块化（分类存储） 和 信息隐藏（操作），函数式编程范式比命令式的优势上在信息隐藏上，在解题可以更抽象，更少关心具体步骤

  函数式编程是面向数学的抽象，函数是指数学中的函数，表示自变量的映射，即源集（ source set ）的目标集（ target set ）之间的对应关系。

> 一句话，函数式程序就是一个表达式。也就是说一个函数的值仅决定于函数参数的值，不依赖其他状态。  
> 比如sqrt(x)函数计算x的平方根，只要x不变，不论什么时候调用，调用几次，值都是不变的。  

  其特点有：

> 函数式编程就是用函数来编程， 返回结果， 没有任何副作用。  
> 函数可以复合为新函数。  
> 函数可以递归调用它自己，但是递归的深度受限于栈的大小。  

### lambda表达式

  ``` java
  BinaryOperator<Integer> add = (x, y) -> x + y;
  ```

> 👉  <kbd>FunctionInterface</kbd> ` BinaryOperator<T> ` 用于定义 Lambda表达式的类型，其实体对象即为一个具体的Lambda表达式  
> 👉  <kbd>Lambda</kbd> ` (x, y) -> x + y ` 是一个匿名方法，对齐C++函数指针指向的函数（把函数当做数据来对待，如作为参数，返回值）

  ``` java
  BinaryOperator<Integer> add = ClassName::methodName;
  ```

> 👉  <kbd>方法引用</kbd> ` ClassName::methodName `，等价Lambda表达式，引用方法的一种语法表示，对齐C++的函数指针  
> 👉  <kbd>Lambda</kbd> 和 <kbd>方法引用</kbd> 可以用于需要函数式接口的地方。  

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
    isNull(txnRequestDTO).or(haveError(txnRequestDTO)).thenThrow(TXN_REQUEST_ERROR);
  ```

### 函数复合+多态
多态高阶函数
  ``` java
    /* 一个多态高阶函数例子 */
    public static <T, U, V> Function<Function<U, V>, Function<Function<T, U>, Function<T, V>>> higherCompose() {
        return f -> g -> t -> f.apply(g.apply(t));
    }

    /* 赋予 lambda参数 一个有意义的名字 */
    public static <T, U, V> Function<Function<U, V>, Function<Function<T, U>, Function<T, V>>> higherCompose2() {
        return tuFunc -> uvFunc -> t -> tuFunc.apply(uvFunc.apply(t));
    }

    /* 给 lambda参数 补上类型信息 */
    public static <T, U, V> Function<Function<U, V>, Function<Function<T, U>, Function<T, V>>> higherCompose3() {
        return (Function<U, V> f) -> (Function<T, U> g) -> (T t) -> f.apply(g.apply(t));
    }

    @Test
    void testFunctionCompose() {
        Function<Double, Integer> f = t -> (int) (t * 3);
        Function<Long, Double> g = t -> t + 2.0;

        assertEquals(Integer.valueOf(9), f.apply((g.apply(1L))));
        assertEquals(Integer.valueOf(9), FunctionTest.<Long, Double, Integer>higherCompose().apply(f).apply(g).apply(1L));
    }
  ```

### 多参函数柯里化


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

> 函数-黑盒模型如下：
> 1. 接受一个参数 : 初始状态
> 2. 内部做一些神秘的事情 ： 转化过程
> 3. 返回一个值 ： 终止状态

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
> - `Stream.collect(Collector collector)`: 使用收集器，进行可变归约操作，收集器是 reduce 方法的模拟
> - `Stream.collect(Supplier supplier,BiConsumer accumulator, BiConsumer combiner)`: 可变归约操作
> - `Stream.count()`: 返回流元素的总个数
> - `Stream.max(Comparator comparator)`: 返回流中的最大元素
> - `Stream.min(Comparator comparator)`: 返回流中的最小元素
> - `Stream.forEach(Consumer action)`: 对每个元素执行action操作
> - `Stream.forEachOrdered(Consumer action)`: 对每个元素执行action操作
> - `Stream.toArray(*)`: 返回一个全部流元素的数组
> - `IntStream.summaryStatistics()`: 汇总所有int流元素的各种摘要数据

     💖 params:
        collector - 收集器
        supplier - 提供商
        accumulator - 累加器
        combiner - 组合器
        dentity – 累加函数的初始值
        action - 消费者

短路操作
> - `Stream.findAny()`: 返回流中任意一个元素
> - `Stream.findFirst()`: 返回流中第一个元素
> - `Stream.anyMatch(Predicate predicate)`: 返回是否匹配，流中存在任意一个元素，匹配predicate条件
> - `Stream.allMatch(Predicate predicate)`: 返回是否匹配，流中所有元素，均匹配predicate条件
> - `Stream.noneMatch(Predicate predicate)`: 返回是否都不匹配，流中所有元素，均不匹配predicate条件

#### 典型参数

##### 函数接口

| 接口                   | 函数原型                | 说明                                |
| --------------------- | ---------------------- | -----------------------------------|
| Predicate\<T>          | boolean test(T t);     | 判断方法，断言，给定参数返回true或false，如：判断对象是否为空          |
| Supplier\<T>           | T get();               | 生产方法，无参执行操作，直接产生结果，如：工厂方法               |
| Consumer\<T>           | void accept(T t);      | 消费方法，给定1个参数执行操作，不产生结果，如：打印日志                |
| BiConsumer<T, U>       | void accept(T t, U u); | 消费方法，给定2个参数执行操作，不产生结果       |
| Function<T,R>          | R apply(T t);          | 映射方法，接受1个参数，并产生结果，如：获得对象的名字，` Student::getAge `      |
| BiFunction<T, U, R>    | R apply(T t, U u);     | 转换方法，接受2个参数，并产生结果                                        |
| UnaryOperator\<T>      | T apply(T t);          | 转化方法，参数与结果类型相同的函数，如一元运算符： 逻辑非（ !）                      |
| BinaryOperator\<T>     | T apply(T t1, T t2);   | 转换方法，参数与结果类型相同的函数，如二元运算符： 求两个数的乘积（ *）               |

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

##### <b>组合排序</b>

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

##### 收集器

> - `Collectors.toCollection(Supplier collectionFactory)`：返回一个收集器，可将流元素转存到指定集合，collectionFactory为集合创建工厂方法
> - `Collectors.toList()`：返回一个收集器，可将流元素转存为一个 List  
> - `Collectors.toSet()`：返回一个收集器，可将流元素转存为一个 Set  
> - `Collectors.joining()`：返回一个收集器，可将流元素连接成一个 String
> - `Collectors.joining(CharSequence delimiter)`：同上，其由指定的分隔符 delimiter 分隔
> - `Collectors.joining(CharSequence delimiter, CharSequence prefix, CharSequence suffix)`：同上，具有指定的前缀和后缀
> - `Collectors.mapping(Function mapper, Collector downstream)`：返回一个收集器，功能等价于Stream.map(mapper).collect(downstream)
> - `Collectors.collectingAndThen(Collector downstream, Function finisher)`：返回一个收集器，再downstream收集器完成最终转化后，再执行finisher功能收尾，功能等价于downstream.finisher().andThen(finisher)
> - `Collectors.count()`：返回一个收集器，可对输入元素计数
> - `Collectors.minBy(Comparator comparator)`：返回一个收集器，可收集流中的最小元素
> - `Collectors.maxBy(Comparator comparator)`：返回一个收集器，可收集流中的最大元素
> - `Collectors.reducing(T identity, BinaryOperator op)`：返回一个收集器，功能等价于Stream.reduce(identity, op).collect(downstream)
> - `Collectors.reducing(T identity, Function mapper, BinaryOperator op)`：返回一个收集器，功能等价于Stream.map(mapper).reduce(identity, op).collect(downstream)

映射关系收集器 ： 将流元素累积到一个Map中，其键和值是将提供的映射函数应用于输入元素的结果
> - `Collectors.toMap(Function keyMapper, Function valueMapper)`
> - `Collectors.toMap(Function keyMapper, Function valueMapper,BinaryOperator mergeFunction)`：mergeFunction为合并函数，用于解决与同一键关联的值之间的冲突
> - `Collectors.toMap(Function keyMapper, Function valueMapper,BinaryOperator mergeFunction, Supplier mapSupplier)`：返回的Map是在mapSupplier中创建的
> - `Collectors.toConcurrentMap(Function keyMapper, Function valueMapper)`：返回的Map是ConcurrentHashMap
> - `Collectors.toConcurrentMap(Function keyMapper, Function valueMapper,BinaryOperator mergeFunction)`：返回的Map是ConcurrentHashMap
> - `Collectors.toConcurrentMap(Function keyMapper, Function valueMapper,BinaryOperator mergeFunction, Supplier mapSupplier)`：返回的Map是在mapSupplier中创建的ConcurrentMap子类
 
分组收集器 ： 其根据分类函数对元素进行分组，并在Map中返回结果
> - `Collectors.groupingBy(Function classifier)`：效果类似：groupingBy(classifier, toList())
> - `Collectors.groupingBy(Function classifier, Collector downstream)`：其分组元素由downstream归约为值
> - `Collectors.groupingBy(Function classifier, Supplier mapFactory, Collector downstream)`：返回的Map是在mapFactory中创建的
> - `Collectors.groupingByConcurrent(Function classifier)`：返回的Map是ConcurrentHashMap
> - `Collectors.groupingByConcurrent(Function classifier, Collector downstream)`： 返回的Map是ConcurrentHashMap
> - `Collectors.groupingByConcurrent(Function classifier, Supplier mapFactory, Collector downstream)`：返回的Map是在mapFactory中创建的ConcurrentMap子类

分块收集器 ： 整理元素流为 Map<Boolean, D>
> - `Collectors.partitioningBy(Predicate predicate)`：根据Predicate对输入元素进行分区，D默认为List
> - `Collectors.partitioningBy(Predicate predicate, Collector downstream)`：同上，其分组元素由downstream归约为值

求和收集器
> - `Collectors.summingInt(ToIntFunction mapper)`：对输入元素，应用 int 值函数
> - `Collectors.summingLong(ToLongFunction mapper)`：对输入元素，应用 long 值函数
> - `Collectors.summingDouble(ToDoubleFunction mapper)`：对输入元素，应用 double 值函数

求算术平均值的收集器
> - `Collectors.averagingInt(ToIntFunction mapper)`：对输入元素，应用 int 值函数
> - `Collectors.averagingLong(ToLongFunction mapper)`：对输入元素，应用 long 值函数
> - `Collectors.averagingDouble(ToDoubleFunction mapper)`：对输入元素，应用 double 值函数

求汇总统计信息的收集器
> - `Collectors.summarizingInt(ToIntFunction mapper)`：对输入元素，应用 int 值函数
> - `Collectors.summarizingLong(ToLongFunction mapper)`：对输入元素，应用 long 值函数
> - `Collectors.summarizingDouble(ToDoubleFunction mapper)`：对输入元素，应用 double 值函数

##### 组合收集器

    `groupingBy(Student::getSex, mapping(Student::getName, toList()))`  

> 💖 groupingBy为主收集器，mapping为下游收集器  
> 💖 同理：Collector也支持嵌套

example:

  ``` java
students.stream()
        .collect(groupingBy(Student::getSex,
                groupingBy(Student::getAge, mapping(Student::getName, toList()))));
  ```

  ``` java
    MALE:
      7:
      - "tom"
      8:
      - "jack"
    FEMALE:
      7:
      - "eva"
  ```


##### 定制收集器

    `groupingBy(Student::getSex, mapping(Student::getName, toList()))`  