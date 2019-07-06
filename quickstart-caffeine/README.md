https://github.com/ben-manes/caffeine




1、Caffeine 的三种缓存填充策略：手动、同步加载和异步加载。


2、Caffeine 有三个值回收策略：基于大小，基于时间和基于引用。
基于大小回收方式假定当缓存大小超过配置的大小限制时会发生回收。 获取大小有两种方法：缓存中计数对象，或获取权重。
基于条目的到期时间，有三种类型：
1、访问后到期 — 从上次读或写发生后，条目即过期。expireAfterAccess
2、写入后到期 — 从上次写入发生之后，条目即过期。expireAfterWrite
3、自定义策略 — 到期时间由 Expiry 实现独自计算

基于引用回收：我们可以将缓存配置启用基于缓存键值的垃圾回收。为此，我们将 key 和 value 配置为 弱引用，并且可以仅配置软引用以进行垃圾回收。
我们应该使用 Caffeine.weakKeys()、Caffeine.weakValues() 和 Caffeine.softValues() 来启用每个选项：


3、刷新：可以将缓存配置为在指定时间段后自动刷新条目。
  这里我们要明白 expireAfter 和 refreshAfter 之间的区别。当请求过期条目时，执行将发生阻塞，直到 build Function 计算出新值为止。
  但是，如果条目可以刷新，则缓存将返回一个旧值，并异步重新加载该值。
  跟expire的区别是，指定时间过后，expire是remove该key，下次访问是同步去获取返回新值；而refresh则是指定时间后，不会remove该key，下次访问会触发刷新，新值没有回来时返回旧值


4、统计：Caffeine 有记录缓存使用情况的统计方式：
     我们也可以传入 recordStats supplier，创建一个 StatsCounter 的实现。每次与统计相关的更改将推送此对象。
     



教程
http://oopsguy.com/2017/10/25/java-caching-caffeine/
https://www.baeldung.com/java-caching-caffeine
https://www.javazhiyin.com/18782.html
https://juejin.im/post/5b8df63c6fb9a019e04ebaf4
https://www.programcreek.com/java-api-examples/?api=com.github.benmanes.caffeine.cache.Caffeine


https://github.com/javagrowing/JGrowing/blob/master/%E5%B8%B8%E7%94%A8%E6%A1%86%E6%9E%B6/%E5%85%B6%E4%BB%96%E6%A1%86%E6%9E%B6/%E6%B7%B1%E5%85%A5%E8%A7%A3%E5%AF%86%E6%9D%A5%E8%87%AA%E6%9C%AA%E6%9D%A5%E7%9A%84%E7%BC%93%E5%AD%98-Caffeine.md

