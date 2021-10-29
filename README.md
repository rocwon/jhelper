# jhelper

### 介绍
jhelper为Java开发人员提供了一系列常用的工具类。如果您不想在每个项目里自己造轮子了，建议您用它。

### 目标
jhelper的设计目标是Simple, Stable, Smart.
- Simple 源文件少、体积小、结构清晰，非必要不依赖第三方Library
- Stable 稳定是对Library的基本要求，谁的生活都不容易，不要给别人制造Bug
- Smart  简洁且语义化的API，没有学习成本，让用户不看文档也能用起来

jhelper中，绝大多数类名都是以er/or结尾的，它们都是您的工具人。简单，稳定，好用，是一个工具人的基本修养。

### 功能
| # | 类名      | 用途                                                                                          |
|---|-----------|---------                                                                                         |
| 1 | Empty     | 静态类。提供了一组is方法，判断各种类型的数组、集合、对象是否为null或empty                                          |
| 2 | Concator  | 静态类。提供了一组concat方法，将两个数组/集合或者多个字符串合并起来                                           |
| 3 | Converter | 静态类。提供了一组to*方法，用于数据类型之间的转换                                                            |
| 4 | Cryptor   | 静态类。提供了一组方法，支持Hash，对称加密以及签名操作                                                                      |
| 5 | Executor  | 静态类。提供了两个execute方法，用于异步执行任务                                                              |
| 6 | Finder    | 静态类。提供了一组find方法，用于在各种类型的数组、集合中进行查找                                               |
| 7 | Guarder   | 静态类。用于生成图片验证码、密码加密、Session生成及Session验证的机制                                            |
| 8 | INIReader | 非静态类。用于处理Windows INI配置文件。提供了一组get*方法获取键值                                      |
| 9 | Reflector | 静态类。以反射的方式设置(setValue)、获取(getValue)对象属性的值。fill/dump实现了map和java bean之间的转换        |
| 10| Requester | 静态类。基于Java Http Client API，封装了常用的get/post 请求                                                 |
| 11| Slicer    | 静态类。提供了一组slice方法，将各种类型的数组、集合进行切片                                                   |
| 12| Sorter    | 静态类。提供了一组sort方法，对各种类型的数组、集合进行排序，支持顺序/倒序，以及并行排序                         |
| 13| Spliter   | 静态类。提供了一组split方法，将一个字符串，按特定的分隔符分割为一系列的子字符串或者整数                         |
| 14| Persister | 非静态类。基于Apache JDBC-Utils，封装了常用的数据库操作，并且支持连接池                                        |
| 15| Pinyin    | 静态类。提供了一个方法 getFirstLetters，提取输入的中文的拼音首字母，比如：“中国人民”->"ZGRM"                  |
| 16| Shell     | 静态类。仿照Unix/Linux Shell风格，提供了一系列文件操作方法，比如cp, mv, write等                              |
| 17| Time      | 静态类。基于JAVA新的日期API，提供了一系列日期操作的方法，比如日期/时间的获取、比较、转换、格式化等               |


