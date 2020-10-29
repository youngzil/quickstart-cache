[Caffeine Github](https://github.com/ben-manes/caffeine)

- [Caffeine的三种缓存填充策略：手动、同步加载和异步加载](#Caffeine的三种缓存填充策略：手动、同步加载和异步加载)
- [Caffeine有三个值回收策略：基于大小，基于时间和基于引用](#Caffeine有三个值回收策略：基于大小，基于时间和基于引用)
- [刷新：可以将缓存配置为在指定时间段后自动刷新条目](#刷新：可以将缓存配置为在指定时间段后自动刷新条目) : （不同于过期）  
- [统计：Caffeine有记录缓存使用情况的统计方式](#统计：Caffeine有记录缓存使用情况的统计方式)


---------------------------------------------------------------------------------------------------------------------
## Caffeine的三种缓存填充策略：手动、同步加载和异步加载

Caffeine Cache提供了三种缓存填充策略：手动、同步加载和异步加载。

1.手动加载
在每次get key的时候指定一个同步的函数，如果key不存在就调用这个函数生成一个值。

2. 同步加载
构造Cache时候，build方法传入一个CacheLoader实现类。实现load方法，通过key加载value。

3. 异步加载
AsyncLoadingCache是继承自LoadingCache类的，异步加载使用Executor去调用方法并返回一个CompletableFuture。异步加载缓存使用了响应式编程模型。

如果要以同步方式调用时，应提供CacheLoader。要以异步表示时，应该提供一个AsyncCacheLoader，并返回一个CompletableFuture。



## Caffeine有三个值回收策略：基于大小，基于时间和基于引用

Caffeine提供了3种回收策略：基于大小回收，基于时间回收，基于引用回收。


1. 基于大小的过期方式
基于大小的回收策略有两种方式：一种是基于缓存大小，一种是基于权重。
基于大小回收方式假定当缓存大小超过配置的大小限制时会发生回收。 获取大小有两种方法：缓存中计数对象，或获取权重。

2.基于时间的过期方式
基于条目的到期时间，Caffeine提供了三种定时驱逐策略：
1、访问后到期 — 从上次读或写发生后，条目即过期。expireAfterAccess
2、写入后到期 — 从上次写入发生之后，条目即过期。expireAfterWrite
3、自定义策略 — 到期时间由 Expiry 实现独自计算

expireAfterAccess(long, TimeUnit):在最后一次访问或者写入后开始计时，在指定的时间后过期。假如一直有请求访问该key，那么这个缓存将一直不会过期。
expireAfterWrite(long, TimeUnit): 在最后一次写入缓存后开始计时，在指定的时间后过期。
expireAfter(Expiry): 自定义策略，过期时间由Expiry实现独自计算。
缓存的删除策略使用的是惰性删除和定时删除。这两个删除策略的时间复杂度都是O(1)。


3. 基于引用的过期方式
基于引用回收：我们可以将缓存配置启用基于缓存键值的垃圾回收。为此，我们将 key 和 value 配置为 弱引用，并且可以仅配置软引用以进行垃圾回收。
我们应该使用 Caffeine.weakKeys()、Caffeine.weakValues() 和 Caffeine.softValues() 来启用每个选项：


Java中四种引用类型
引用类型	被垃圾回收时间	用途	生存时间
强引用 Strong Reference	从来不会	对象的一般状态	JVM停止运行时终止
软引用 Soft Reference	在内存不足时	对象缓存	内存不足时终止
弱引用 Weak Reference	在垃圾回收时	对象缓存	gc运行后终止
虚引用 Phantom Reference	从来不会	可以用虚引用来跟踪对象被垃圾回收器回收的活动，当一个虚引用关联的对象被垃圾收集器回收之前会收到一条系统通知	JVM停止运行时终止



## 刷新：可以将缓存配置为在指定时间段后自动刷新条目
  这里我们要明白 expireAfter 和 refreshAfter 之间的区别：
  当请求过期条目时，执行将发生阻塞，直到 build Function 计算出新值为止。
  但是，如果条目可以刷新，则缓存将返回一个旧值，并异步重新加载该值。
  
  跟expire的区别是，指定时间过后，expire是remove该key，下次访问是同步去获取返回新值；
  而refresh则是指定时间后，不会remove该key，下次访问会触发刷新，新值没有回来时返回旧值




## 统计：Caffeine 有记录缓存使用情况的统计方式

 我们也可以传入 recordStats supplier，创建一个 StatsCounter 的实现。每次与统计相关的更改将推送此对象。
 与Guava Cache的统计一样。
 通过使用Caffeine.recordStats(), 可以转化成一个统计的集合. 通过 Cache.stats() 返回一个CacheStats。
 CacheStats提供以下统计方法：
     hitRate(): 返回缓存命中率
     evictionCount(): 缓存回收数量
     averageLoadPenalty(): 加载新值的平均时间



教程
https://www.cnblogs.com/rickiyang/p/11074158.html
http://oopsguy.com/2017/10/25/java-caching-caffeine/
https://www.baeldung.com/java-caching-caffeine
https://www.javazhiyin.com/18782.html
https://juejin.im/post/5b8df63c6fb9a019e04ebaf4
https://www.programcreek.com/java-api-examples/?api=com.github.benmanes.caffeine.cache.Caffeine


https://github.com/javagrowing/JGrowing/blob/master/%E5%B8%B8%E7%94%A8%E6%A1%86%E6%9E%B6/%E5%85%B6%E4%BB%96%E6%A1%86%E6%9E%B6/%E6%B7%B1%E5%85%A5%E8%A7%A3%E5%AF%86%E6%9D%A5%E8%87%AA%E6%9C%AA%E6%9D%A5%E7%9A%84%E7%BC%93%E5%AD%98-Caffeine.md


---------------------------------------------------------------------------------------------------------------------




