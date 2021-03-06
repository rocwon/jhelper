# jhelper

### 介绍
jhelper为Java开发人员提供了一系列常用的工具类。如果您不想在项目里自己造轮子了，建议您用它。

### 理念
jhelper的设计目标是Simple, Stable, Smart.
- Simple 源文件少、体积小、结构清晰，非必要不依赖第三方Library
- Stable 稳定是对Library的基本要求，谁的生活都不容易，不要给别人制造Bug
- Smart  简洁且语义化的API，没有学习成本，让用户不看文档也能用起来

jhelper中，绝大多数类名都是以er/or结尾的，它们都是您的工具人。简单、稳定、好用，是一个工具人的基本修养。正如您所见，jhelper与常见的套路不同，
没有StringUtils、CollectionUtils这样的东西，而是以“行为”为主题组织API，想编解码就用Codec，想查找就用Finder，想排序就用Sorter。

### JDK版本
Java 10+, 最好是Java 11；不支持Java 8及以下版本。我们强烈建议开发者与时俱进，升级到Java 11 LTS。

### 第三方依赖
jhelper以尽量不依赖第三方为开发原则。但有一些久经考验的、优秀的第三方库，真的值得采用。
- 如果您要用到Codec，则需要在您的项目中引入jackson、msgpack
- 如果您要用到Persister，则需要在您的项目中引入Apache-DbUtils和HikariCP

### 功能
| # | 类名      | 用途                                                                                          |
|---|-----------|---------                                                                                         |
| 1 | Empty     | 提供了一组is静态方法，判断各种类型的数组、集合、对象是否为null或empty                                          |
| 2 | Codec     | 依赖于JACKSON, 提供了一组静态to方法和decode方法，对Base64, JSON和MSGPACK进行编解码                     |
| 3 | Cacher    | 依赖JEDIS,提供了若干便捷的静态方法访问REDIS，甚至不需要了解REDIS的基本知识
| 4 | Concator  | 提供了一组concat静态方法，将两个数组/集合或者多个字符串合并起来                                           |
| 5 | Converter | 提供了一组to*静态方法，用于数据类型之间的转换                                                            |
| 6 | Cryptor   | 提供了一组静态方法，支持Hash，对称加密以及签名操作                                                                      |
| 7 | Executor  | 提供了两个execute静态方法，用于异步执行任务                                                              |
| 8 | Finder    | 提供了一组find静态方法，用于在各种类型的数组、集合中进行查找                                               |
| 9 | Guarder   | 静态。用于生成图片验证码、密码加密、Session生成及Session验证的机制                                            |
| 10 | INIReader | 非静态。用于处理Windows INI配置文件。提供了一组get*方法获取键值                                      |
| 11 | Reflector | 静态。以反射的方式设置(setValue)、获取(getValue)对象属性的值。fill/dump实现了map和java bean之间的转换        |
| 12| Requester | 静态。基于Java Http Client API，封装了get/post请求                                                 |
| 13| Slicer    | 提供了一组slice静态方法，将各种类型的数组、集合进行切片                                                   |
| 14| Sorter    | 提供了一组sort静态方法，对各种类型的数组、集合进行排序，支持顺序/倒序，以及并行排序                         |
| 15| Spliter   | 提供了一组split静态方法，将一个字符串，按特定的分隔符分割为一系列的子字符串或者整数                         |
| 16| Persister | 依赖于Apache DbUtils和Hikari，封装了常用的数据库操作，并且支持连接池                                       |
| 17| Pinyin    | 提供了一个静态方法 getFirstLetters，提取输入的中文的拼音首字母，比如：“中国人民”->"ZGRM"                  |
| 18| Time      | 基于JAVA新的日期API，提供了一系列日期操作的静态方法，比如日期/时间的获取、比较、转换、格式化等               |


