# jhelper用户手册（for 0.1.x）

## 介绍
jhelper是一个为Java程序员准备的工具类集合。它提供了日常开发中经常用到的、而标准库没有的API，或者标准库的API太过于偏向底层，学习难度高，或者易用性不足够好。
根据我们的经验，这些没太多技术含量又不解决业务问题的胶水代码，存在如下两个问题：

- 项目中随处用到就随手一写，重复代码太多
- 最容易引发Bug，导致软件质量下降，维护困难

有几年经验的程序员，开始意识到这个问题了，于是就在项目中建一个*.utils包，里面包罗万象，既有网上搜来的代码，还有自家祖传的代码，也有从项目里重构提取出来的代码。虽然这种方式解决了项目中的问题，但解决得并不优雅。
这就是我们编写jhelper的初衷，希望它对您有一点用处。

## 功能

jhelper聚焦于“行为”，将相同、相似的行为组织在一个类里，并以er/or作为类名的后缀(也不全是如此)，开发人员瞄一眼就知道它的用途。0.1.x版本包含了17个工具，如下表所示：

|  #   | 类名      | 用途                                                         |
| :--: | --------- | ------------------------------------------------------------ |
|  1   | Empty     | 提供了一组is静态方法，判断各种类型的数组、集合、对象是否为null或empty |
|  2   | Codec     | 依赖于JACKSON, 提供了一组静态to方法和decode方法，对Base64, JSON和MSGPACK进行编解码 |
|  3   | Concator  | 提供了一组concat静态方法，将两个数组/集合或者多个字符串合并起来 |
|  4   | Converter | 提供了一组to*静态方法，用于数据类型之间的转换                |
|  5   | Cryptor   | 提供了一组静态方法，支持Hash，对称加密以及签名操作           |
|  6   | Executor  | 提供了两个execute静态方法，用于异步执行任务                  |
|  7   | Finder    | 提供了一组find静态方法，用于在各种类型的数组、集合中进行查找 |
|  8   | Guarder   | 静态。用于生成图片验证码、密码加密、Session生成及Session验证的机制 |
|  9   | INIReader | 非静态。用于处理Windows INI配置文件。提供了一组get*方法获取键值 |
|  10  | Reflector | 静态。以反射的方式设置(setValue)、获取(getValue)对象属性的值。fill/dump实现了map和java bean之间的转换 |
|  11  | Requester | 静态。基于Java Http Client API，封装了get/post请求           |
|  12  | Slicer    | 提供了一组slice静态方法，将各种类型的数组、集合进行切片      |
|  13  | Sorter    | 提供了一组sort静态方法，对各种类型的数组、集合进行排序，支持顺序/倒序，以及并行排序 |
|  14  | Spliter   | 提供了一组split静态方法，将一个字符串，按特定的分隔符分割为一系列的子字符串或者整数 |
|  15  | Persister | 依赖于Apache DbUtils和Hikari，封装了常用的数据库操作，并且支持连接池 |
|  16  | Pinyin    | 提供了一个静态方法 getFirstLetters，提取输入的中文的拼音首字母，比如：“中国人民”->"ZGRM" |
|  17  | Time      | 基于JAVA新的日期API，提供了一系列日期操作的静态方法，比如日期/时间的获取、比较、转换、格式化等 |

## API
### 1. Empty
Empty类提供了一组静态方法，它们的作用包括：
- 判断输入参数是否为empty

- 生成可变的、不改变的list, set, map

- 为字符串去除首尾的空白字符

  

  那么， 究竟什么是empty? jhelper定义了如下规则：

- Object为null， 即: if(object == null)

- 数组的长度为0，即：if(array.length == 0)

- 集合(list, set, map)的长度为0，即: if(collection.isEmpty())

- 字符串经过trim之后，长度为0，即：if(str.trim().length() == 0)

- 数字类型(基础类型以及从Number派生出来)的值为0，0f, 0l, 0d

| #    | 方法                                           | 说明                                                         |
| ---- | ---------------------------------------------- | ------------------------------------------------------------ |
| 1    | static boolean zero(Number arg)                | 测试输入的数字是否为0                                        |
| 2    | static boolean is(Object arg)                  | 测试输入的对象是否为null                                     |
| 3    | static Object is(Object arg, Object orElseOne) | 如果参数arg不为null则返回arg, 否则返回 orElseOne对象         |
| 4    | static boolean oneOf(Object... args)           | 参数args是个可变长对象数组。只要args中任一元素为null，则返回true |
| 5    | static boolean allOf(Object... args)           | 参数数组args中所有元素都为null，则返回true，否则返回false    |
| 6    | static boolean is(String arg)                  | 测试字符串参数arg是否为null，或者去掉空白字符之后长度为0     |
| 7    | static boolean is(Object[] objs)               | 测试对象数组objs是否为null， 或者length等于0                 |
| 8    | static boolean is(int[] args)                  | 测试int数组是否为null，或者length等于0                       |
| 9    | static boolean is(long[] args)                 | 测试long数组是否为null， 或者length等于0                     |
| 10   | static boolean is(float[] args)                | 测试float数组是否为null，或者length等于0                     |
| 11   | static boolean is(double[] args)               | 测试double数组是否为null，或者长度为0                        |
| 12   | static boolean is(boolean[] args)              | 测试boolean数组是否为null，或者长度为0                       |
| 13   | static boolean is(byte[] args)                 | 测试byte数组是否为null，或者长度为0                          |
| 14   | static boolean is(char[] args)                 | 测试char数组是否为null，或者长度为0                          |
| 15   | static boolean is(short[] args)                | 测试short数组是否为null，或者长度为0                         |
| 16   | static boolean is(Collection<?> arg)           | 测试集合(List, Set)是否为null，或者长度为0（arg.isEmpty() == true） |
| 17   | static boolean is(Map<?, ?> arg)               | 测试Map是否为null，或者长度为0(arg.isEmpty() == true)        |
| 18   | static<T> List<T> list()                       | 返回一个长度为0的ArrayList对象，支持泛型                     |
| 19   | static<T> List<T> immutableList()              | 返回一个长度为0的只读的List对象，不可修改，支持泛型          |
| 20   | static<T> Set<T> set()                         | 返回一个长度为0的HashSet对象，支持泛型                       |
| 21   | static<T> Set<T> immutableSet()                | 返回一个长度为0的只读的HashSet对象，不可修改，支持泛型       |
| 22   | static<K, V> Map<K, V> map()                   | 返回一个长度为0的HashMap对象，支持泛型                       |
| 23   | public static<K, V> Map<K, V> immutableMap()   | 返回一个长度为0的只读的HashMap对象，不可修改，支持泛型       |
| 24   | static String trim(String arg)                 | 删除字符串参数arg左右的空白字符                              |

### 2. Concator

Concator类提供了一系列concat静态方法，可以将两个相同类型的东西连接起来，比如：

[1,2,3] concat [4,5,6] ->[1,2,3,4,5,6]， "My name" concat "is Zhang san" -> "My name is Zhang san"

| #    | 方法                                                         | 说明                                                         |
| ---- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 1    | static int[] concat(int[] array1, int[] array2)              | 将两个int数组合并起来返回一个新的int数组。如果二者都为null，则返回null |
| 2    | static float[] concat(float[] array1, float[] array2)        | 将两个float数组合并起来返回一个新的int数组。如果二者都为null，则返回null |
| 3    | static long[] concat(long[] array1, long[] array2)           | 将两个long数组合并起来返回一个新的int数组。如果二者都为null，则返回null |
| 4    | static double[] concat(double[] array1, double[] array2)     | 将两个double数组合并起来返回一个新的int数组。如果二者都为null，则返回null |
| 5    | public static String[] concat(String[] array1, String[] array2) | 将两个String数组合并起来返回一个新的int数组。如果二者都为null，则返回null |
| 6    | static Object[] concat(Object[] array1, Object[] array2)     | 将两个Object数组合并起来返回一个新的int数组。如果二者都为null，则返回null |
| 7    | static boolean[] concat(boolean[] array1, boolean[] array2)  | 将两个boolean数组合并起来返回一个新的int数组。如果二者都为null，则返回null |
| 8    | static byte[] concat(byte[] array1, byte[] array2)           | 将两个byte数组合并起来返回一个新的int数组。如果二者都为null，则返回null |
| 9    | static char[] concat(char[] array1, char[] array2)           | 将两个char数组合并起来返回一个新的int数组。如果二者都为null，则返回null |
| 10   | static String concat(String... args)                         | 将可变参数args数组包含的字符串，逐个拼接起来，与String.concat作用相同 |
| 11   | static String concat(Collection<String> args)                | 将集合中的所有字符串，逐个拼接起来，与String.concat作用相同  |
| 12   | static<T> List<T> concat(List<T> arg0, List<T> arg1)         | 将两个List的元素合并起来，arg1的元素被追加在arg0的末尾       |
| 13   | static<T> Set<T> concat(Set<T> arg0, Set<T> arg1)            | 将两个Set的元素合并起来，相同元素会被替代，且无法确保顺序    |
| 14   | static<K, V> Map<K, V> concat(Map<K, V> arg0, Map<K, V> arg1) | 将两个Map的元素合并起来，arg0中相同的key会被替代为arg1中对应的值 |

### 3. Converter
Converter类用于对相关数据类型进行转换，包含四类常见的：
- 数字和字符串之间的转换
- 位操作时整数与字节数组之间的转换
- Java Bean(POJO)与Map之间的转换
- 将Object转换为指定的类型（泛型）
- 字符串和日期之间的转换

数据类型转换，不可避免有异常发生。jhelper的处理原则是尽量不再抛出异常，让开发者的代码干净一些。因此遵循以下规则：

- 字符串转数字，凡是发生异常了(格式错误)，均返回0
- 对象转换时，如果输入的参数为null，则返回null
- 字节转整数时，如果字节长度错误（比如4个byte对应一个int），则返回0；

| #    | 方法 | 说明 |
| ---- | ---- | ---- |
|      |      |      |
|      |      |      |
|      |      |      |
|      |      |      |
|      |      |      |
|      |      |      |
|      |      |      |
|      |      |      |
|      |      |      |
|      |      |      |
|      |      |      |
|      |      |      |
|      |      |      |
|      |      |      |
|      |      |      |

