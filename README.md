原项目地址
https://gitee.com/youngzil/quickstart-all


J2Cache
Java 两级缓存框架，可以让应用支持两级缓存框架 ehcache(Caffeine) + redis 。避免完全使用独立缓存系统所带来的网络IO开销问题
https://gitee.com/ld/J2Cache

J2Cache 是 OSChina 目前正在使用的两级缓存框架（要求至少 Java 8）。第一级缓存使用内存(同时支持 Ehcache 2.x、Ehcache 3.x 和 Caffeine)，第二级缓存使用 Redis(推荐)/Memcached 。 由于大量的缓存读取会导致 L2 的网络成为整个系统的瓶颈，因此 L1 的目标是降低对 L2 的读取次数。 该缓存框架主要用于集群环境中。单机也可使用，用于避免应用重启导致的缓存冷启动后对后端业务的冲击。


Java Caching(缓存)-策略和JCache API
https://blog.csdn.net/boonya/article/details/54632129



RocksDB
http://rocksdb.org/
https://github.com/facebook/rocksdb


https://rocksdb.org.cn/


Coherence是一个分布式的缓存方案，并且通过集群为应用提供强大的缓存后备支持。Coherence主要是内存缓存，即存储区域主要在内存当中。 
与一般的分布式缓存方案如JBossCache, Memcache 等相同，分布式缓存的价值基于网络IO性能高于DB查询的磁盘IO性能这样一个特点。 
Coherence所有的设计都是基于多个（可以是非常多）的JVM，很多Coherence的测试都是使用几十甚至上百个节点来进行的。 


缓存框架
https://www.oschina.net/project/tag/132/cachesystem?sort=view
http://developer.51cto.com/art/201411/457423.htm



