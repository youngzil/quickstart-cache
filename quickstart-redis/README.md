相关参考资料
官网： https://redis.io     
中文网：http://www.redis.cn/    
文档：http://redisdoc.com/
https://www.oschina.net/p/redis


https://redis.io/topics/quickstart

http://doc.redisfans.com/
http://www.redis.net.cn/order/

在线测试redis命令 http://try.redis.io/
官方文档 http://redis.io/documentation 
https://redis.io/commands
Redis 支持的客户端编程语言  http://redis.io/clients

Redis下载：https://redis.io/download


Redis桌面管理器，可从 http://redisdesktop.com/download 下载该软件包，安装即可
https://redisdesktop.com/
https://github.com/uglide/RedisDesktopManager/

github：https://github.com/antirez/redis
github文档：https://github.com/antirez/redis-doc

http://www.yiibai.com/redis/redis_quick_guide.html

中文网：
http://doc.redisfans.com/
http://www.redis.net.cn/
http://www.redis.cn/


Redis教程：
http://www.runoob.com/redis/redis-tutorial.html
http://www.yiibai.com/redis/


Redis集群教程：
http://www.redis.cn/topics/cluster-tutorial.html
客户端如何选择见：http://redis.io/clients


redis java 客户端：
https://github.com/xetorthio/jedis
https://github.com/redisson/redisson
https://github.com/caoxinyu/RedisClient
https://github.com/alphazero/jredis



Redis3.X开始提供集群功能，和哨兵监控功能？？？

Redis支持五种数据类型：string（字符串），hash（哈希），list（列表），set（集合）及zset(sorted set：有序集合)。

Redis 集群的数据分片
Redis 集群没有使用一致性hash, 而是引入了 哈希槽的概念.
Redis 集群有16384个哈希槽,每个key通过CRC16校验后对16384取模来决定放置哪个槽.集群的每个节点负责一部分hash槽,举个例子,比如当前集群有3个节点,那么:
节点 A 包含 0 到 5500号哈希槽.
节点 B 包含5501 到 11000 号哈希槽.
节点 C 包含11001 到 16384号哈希槽.
这种结构很容易添加或者删除节点. 比如如果我想新添加个节点D, 我需要从节点 A, B, C中得部分槽到D上. 如果我像移除节点A,需要将A中得槽移到B和C节点上,然后将没有任何槽的A节点从集群中移除即可. 由于从一个节点将哈希槽移动到另一个节点并不会停止服务,所以无论添加删除或者改变某个节点的哈希槽的数量都不会造成集群不可用的状态.

Redis 集群的主从复制模型:
当某个节点不可用，会造成整个集群不可用（某个节点的master和slave都不可用）

Redis 一致性保证
Redis 并不能保证数据的强一致性. 这意味这在实际中集群在特定的条件下可能会丢失写操作.

下面是一个最少选项的集群的配置文件:
port 7000
cluster-enabled yes
cluster-config-file nodes.conf
cluster-node-timeout 5000
appendonly yes
文件中的 cluster-enabled 选项用于开实例的集群模式， 而 cluster-conf-file 选项则设定了保存节点配置文件的路径， 默认值为 nodes.conf.节点配置文件无须人为修改， 它由 Redis 集群在启动时创建， 并在有需要时自动进行更新。
要让集群正常运作至少需要三个主节点，不过在刚开始试用集群功能时， 强烈建议使用六个节点： 其中三个为主节点， 而其余三个则是各个主节点的从节点。


集群监控哨兵：
参考文章：https://www.cnblogs.com/jaycekon/p/6237562.html

启动Sentinel
/sentinel$ redis-sentinel sentinel.conf

Sentinel的工作方式:
1)：每个Sentinel以每秒钟一次的频率向它所知的Master，Slave以及其他 Sentinel 实例发送一个 PING 命令 
2)：如果一个实例（instance）距离最后一次有效回复 PING 命令的时间超过 down-after-milliseconds 选项所指定的值， 则这个实例会被 Sentinel 标记为主观下线。 
3)：如果一个Master被标记为主观下线，则正在监视这个Master的所有 Sentinel 要以每秒一次的频率确认Master的确进入了主观下线状态。 
4)：当有足够数量的 Sentinel（大于等于配置文件指定的值）在指定的时间范围内确认Master的确进入了主观下线状态， 则Master会被标记为客观下线 
5)：在一般情况下， 每个 Sentinel 会以每 10 秒一次的频率向它已知的所有Master，Slave发送 INFO 命令 
6)：当Master被 Sentinel 标记为客观下线时，Sentinel 向下线的 Master 的所有 Slave 发送 INFO 命令的频率会从 10 秒一次改为每秒一次 
7)：若没有足够数量的 Sentinel 同意 Master 已经下线， Master 的客观下线状态就会被移除。 
若 Master 重新向 Sentinel 的 PING 命令返回有效回复， Master 的主观下线状态就会被移除。





Redis 优势
性能极高 – Redis能读的速度是110000次/s,写的速度是81000次/s 。
丰富的数据类型 – Redis支持二进制案例的 Strings, Lists, Hashes, Sets 及 Ordered Sets 数据类型操作。
原子 – Redis的所有操作都是原子性的，意思就是要么成功执行要么失败完全不执行。单个操作是原子性的。多个操作也支持事务，即原子性，通过MULTI和EXEC指令包起来。
丰富的特性 – Redis还支持 publish/subscribe, 通知, key 过期等等特性。

Redis 是一个高性能的key-value数据库。 redis的出现，很大程度补偿了memcached这类keyvalue存储的不足，在部 分场合可以对关系数据库起到很好的补充作用。它提供了Python，Ruby，Erlang，PHP客户端，使用很方便。

性能测试结果：
SET操作每秒钟 110000 次，GET操作每秒钟 81000 次，服务器配置如下：
Linux 2.6, Xeon X3320 2.5Ghz.
stackoverflow 网站使用 Redis 做为缓存服务器。

Installation
Download, extract and compile Redis with:
$ wget http://download.redis.io/releases/redis-4.0.2.tar.gz
$ tar xzf redis-4.0.2.tar.gz
$ cd redis-4.0.2
$ make
The binaries that are now compiled are available in the src directory. Run Redis with:
$ src/redis-server
You can interact with Redis using the built-in client:
$ src/redis-cli
redis> set foo bar
OK
redis> get foo
"bar"

