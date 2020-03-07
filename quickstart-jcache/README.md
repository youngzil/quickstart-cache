https://github.com/jsr107/jsr107spec

Java缓存规范JCache API（JSR107），它对Java对象缓存进行标准化，方便高效开发，让程序员摆脱实现缓存有效期、互斥、假脱机（spooling）和缓存一致性等负担。该规范提供了API、RI（参考实现）和TCK（技术兼容性套件）。  
  
Java Caching定义了5个核心接口，分别是CachingProvider, CacheManager, Cache, Entry 和 Expiry。  
CachingProvider定义了创建、配置、获取、管理和控制多个CacheManager。一个应用可以在运行期访问多个CachingProvider。  
CacheManager定义了创建、配置、获取、管理和控制多个唯一命名的Cache，这些Cache存在于CacheManager的上下文中。一个CacheManager仅被一个CachingProvider所拥有。  
Cache是一个类似Map的数据结构并临时存储以Key为索引的值。一个Cache仅被一个CacheManager所拥有。  
Entry是一个存储在Cache中的key-value对。  
每一个存储在Cache中的条目有一个定义的有效期，即Expiry Duration。一旦超过这个时间，条目为过期的状态。一旦过期，条目将不可访问、更新和删除。缓存有效期可以通过ExpiryPolicy设置。  
  
  

Java缓存规范JCache API（JSR107），它对Java对象缓存进行标准化，方便高效开发，让程序员摆脱实现缓存有效期、互斥、假脱机（spooling）和缓存一致性等负担。该规范提供了API、RI（参考实现）和TCK（技术兼容性套件）。
从设计的角度看，基本组成部分有一个CacheManager，用来持有、控制缓存集合。缓存里存放键值对条目。
整个规范包括了如下内容：
支持原子操作的缓存读写
缓存事件监听器
统计
事务
注解

JSR107从2001年开始，中间搁置了一段时间，后来Terracotta（产品：EhCache）和Oracle（产品：Coherence）在2010年加强了对JSR-107的投入，原本有望放入JAVAEE 7(JSR342)中，可惜在期限内完不成，直到2012年底才推出了草案。

我更关注数据网格（JSR347），那个是JSR107的超集，关注缓存的逐出、复制和分布化，以及事务。可惜连草案也还没影呢。

http://jcp.org/en/jsr/detail?id=107
http://jcp.org/en/jsr/detail?id=347





参考
https://www.baeldung.com/jcache



