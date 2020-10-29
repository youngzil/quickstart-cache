- [ConcurrentLinkedHashMap介绍](#ConcurrentLinkedHashMap介绍)
- [ConcurrentLinkedHashMap实现基于LRU策略的缓存](#ConcurrentLinkedHashMap实现基于LRU策略的缓存)
- [CompactMap介绍](#CompactMap介绍)



---------------------------------------------------------------------------------------------------------------------

## ConcurrentLinkedHashMap介绍

[ConcurrentLinkedHashMap Github](https://github.com/ben-manes/concurrentlinkedhashmap)

ConcurrentLinkedHashMap是java.util.LinkedHashMap的一个高性能实现。主要用于软件缓存。

ConcurrentLinkedHashMap已经被合并到了Guava包中

Caffeine is the Java 8 successor to ConcurrentLinkedHashMap and Guava's cache. Projects should prefer Caffeine and migrate when requiring JDK8 or higher. The previous caching projects are supported in maintenance mode.

咖啡因是ConcurrentLinkedHashMap和Guava缓存的Java 8继承者。当需要JDK8或更高版本时，项目应首选Caffeine并迁移。维护模式支持以前的缓存项目。

由于项目中一直使用的Google的ConcurrentLinkedHashMap作为JVM缓存的容器，该项目已经很久没有更新，从作者的官方文档中看到ConcurrentLinkedHashMap已经被合并到了Guava包中，推荐使用Guava的MapMaker和CacheBuilder，本文稍微简述一下两者的用法。

正如作者推荐的，在大部分情况下我们应该使用Guava中的CacheBuilder来替代ConcurrentLinkedHashMap，因为基于同样的算法，同样效率，CacheBuilder具有更简洁易用API，并且Guava的维护团队更加活跃，一个简洁、活跃的开源项目总数比一个已经不经常更新的项目要好。


ConcurrentLinkedHashMap提供一个基于权重管理容量的Map，有以下特性
- 基于LRU（Least recently used，最近最少使用）算法来替换Map中的元素
- 再高负载情况下，和ConrrentHashMap具有相同的性能
- Can bound by the size of the values (e.g. Multimap cache) （这条没看懂）
- 提供元素移除的通知事件



---------------------------------------------------------------------------------------------------------------------

## ConcurrentLinkedHashMap实现基于LRU策略的缓存

ConcurrentLinkedHashMap 是google团队提供的一个容器。它有什么用呢？其实它本身是对ConcurrentHashMap的封装，可以用来实现一个基于LRU策略的缓存。

[详细介绍可以参见](http://code.google.com/p/concurrentlinkedhashmap)


这个结构必须满足访问修改快速，并发性好，占用内存空间少，存储的个数大小可控制，并且可以驱逐访问量少的entry，从而可以达到实时统计热点的查询数字。
- 2.1.a 支持并发。它是实现了ConcurrentMap接口的。基本上它的实现是遵循concurrentMap的思想的。
- 2.1.b 它把我们普通concurrenthashmap的segments用一个额外的双链表维护起来，通过操作这个链表，里面的entry可以在O(1)的时间之类删除或者重新排序。当一个entry被访问，则被移动到表头；当这个map的容量超过预设，则移除位于链表尾的entry。这样就可以保证表头是最新被使用的entry，表尾是最近最少被使用的entry
- 2.1.c 从另外一个角度来说，可以把ConcurrentLinkedHashMap 分成两个view. 一个是同步的，一个是异步的。Hashtable对于调用方来说是同步的，链表对于调用方式不透明的，异步的。
- 2.1.d 性能和concurrenthashmap的比较, 肯定比concurrenthashmap差，但是属于可以忍受的范围。

综上所述，我们可以利用这个LRU concurrent map link,保证我们在单位时间内，仅保存有限数量访问量较高的key, 最近比较少访问的key,我们则可以认为它不是热点，当map的Size达到上限之后，清除这个key


LRU（Least recently used，最近最少使用）算法根据数据的历史访问记录来进行淘汰数据，其核心思想是“如果数据最近被访问过，那么将来被访问的几率也更高”。



参考
http://fortl.net/2016/Google%E7%9A%84ConcurrentLinkedHashMap%E5%92%8CGuava%E7%9A%84Cache/
http://jm.taobao.org/2016/02/01/3744/


---------------------------------------------------------------------------------------------------------------------
## CompactMap介绍

compactmap依赖exx-collections-0.2.jar

在项目空闲期，针对Swift中的Map、CompactMap、Filter、Reduce进行系统学习

最初这个提案用了filterMap这个名字，但后来经过讨论，就决定参考了 Ruby 的Array::compact方法，使用compactMap这个名字。

没找到相关资料


参考  
[Swift之Map与CompactMap区别](https://www.jianshu.com/p/07b59f4f0071)
[Apple开发者文档compactMap](https://developer.apple.com/documentation/swift/sequence/2950916-compactmap)


