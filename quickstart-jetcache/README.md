https://github.com/alibaba/jetcache


其他和SpringBoot 和 Spring 的结合例子可以查看源码中的samples


介绍
JetCache是​​一种Java缓存抽象，可为各种缓存解决方案提供一致的使用。它提供了比Spring Cache更强大的注释。JetCache中的注释支持本机TTL，两级缓存和分布式自动刷新，您也可以Cache手动操作实例代码。目前有四种工具：RedisCache，TairCache（在github上不开源）， CaffeineCache（在内存中），简单LinkedHashMapCache（在内存中）。

JetCache的全部功能：
    通过一致的Cache API操作缓存。
    使用带有TTL（生存时间）的注释和两级缓存支持的声明性方法缓存
    Cache使用注释创建和配置实例
    自动收集Cache实例和方法缓存的访问统计信息
    密钥生成和价值序列化的策略可以定制
    分布式缓存自动刷新和分布式锁定。（2.2+）
    使用Cache API（2.2+，带有redis生菜客户端）的异步访问
    Spring Boot支持
    
    
    
JetCache提供的核心能力包括：
    提供统一的，类似jsr-107风格的API访问Cache，并可通过注解创建并配置Cache实例
    通过注解实现声明式的方法缓存，支持TTL和两级缓存
    分布式缓存自动刷新，分布式锁 (2.2+)
    支持异步Cache API
    Spring Boot支持
    Key的生成策略和Value的序列化策略是可以定制的
    针对所有Cache实例和方法缓存的自动统计

