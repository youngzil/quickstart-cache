Guava's cache 
https://github.com/google/guava/wiki/CachesExplained
http://fortl.net/2016/Google%E7%9A%84ConcurrentLinkedHashMap%E5%92%8CGuava%E7%9A%84Cache/



guava缓存的expireAfterWrite与refreshAfterWrite的区别

expireAfterWrite是在指定项在一定时间内没有创建/覆盖时，会移除该key，下次取的时候从loading中取
expireAfterAccess是指定项在一定时间内没有读写，会移除该key，下次取的时候从loading中取
refreshAfterWrite是在指定时间内没有被创建/覆盖，则指定时间过后，再次访问时，会去刷新该缓存，在新值没有到来之前，始终返回旧值
跟expire的区别是，指定时间过后，expire是remove该key，下次访问是同步去获取返回新值；而refresh则是指定时间后，不会remove该key，下次访问会触发刷新，新值没有回来时返回旧值



由于项目中一直使用的Google的ConcurrentLinkedHashMap作为JVM缓存的容器，该项目已经很久没有更新，从作者的官方文档中看到ConcurrentLinkedHashMap已经被合并到了Guava包中，推荐使用Guava的MapMaker和CacheBuilder，本文稍微简述一下两者的用法。

正如作者推荐的，在大部分情况下我们应该使用Guava中的CacheBuilder来替代ConcurrentLinkedHashMap，因为基于同样的算法，同样效率，CacheBuilder具有更简洁易用API，并且Guava的维护团队更加活跃，一个简洁、活跃的开源项目总数比一个已经不经常更新的项目要好。





示例参考
http://ifeve.com/google-guava-cachesexplained/
https://www.baeldung.com/guava-cache
https://segmentfault.com/a/1190000011105644

https://www.jianshu.com/p/64b0df87e51b