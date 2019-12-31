批量字节操作Bitmap
Redis部署模式：单节点，单节点主从、主从带Sentinel哨兵模式、集群模式
Redis集群模式搭建、扩缩容：手动 和 ruby脚本
主从带Sentinel哨兵模式:Sentinel的工作方式
Redis支持五种数据类型：string（字符串），hash（哈希），list（列表），set（集合）及zset(sorted set：有序集合)。
Redis命令：Key（键）、String（字符串）、Hash（哈希表）、List（列表）、Set（集合）、SortedSet（有序集合）、Pub/Sub（发布/订阅）、Transaction（事务）

Redis中支持RDB和AOF这两种持久化机制
redis提供6种数据淘汰策略
Redis服务端处理流程：计算槽、槽节点查找、处理/ASK/MOVED

Redis和Memcached区别：支持数据类型、持久化、数据备份恢复、路由规则、网络IO模型


Redis如何解决key冲突：使用redis的不同db（集群模式部署不行），key带上业务含义
如何解决Redis的并发竞争key问题：Redis事务、分布式锁


Redis集群解决方案：
Redis 的集群解决方案有社区的，也有官方的，社区的解决方案有 Codis 和Twemproxy,
Codis是由我国的豌豆荚团队开源的，
Twemproxy是Twitter团队的开源的；
官方的集群解决方案就是 Redis Cluster，这是由 Redis 官方团队来实现的。


Redis的最常被问到知识点总结.md

Redis的五种数据类型的实现是什么数据结构

Redis主从复制机制



---------------------------------------------------------------------------------------------------------------------
参考
https://blog.csdn.net/men_wen/article/category/6769467
https://github.com/menwengit/redis_source_annotation

分布式缓存Redis使用心得：
https://www.cnblogs.com/yangxiaolan/p/5786123.html

参考
http://redisdoc.com/topic/cluster-tutorial.html
https://juejin.im/post/5c1bb40a6fb9a049f36211b0
http://redisdoc.com/topic/cluster-spec.html




集群中单独运行的节点称为孤儿节点


从redis.conf文件可以看到redis默认有16个数据库，可以通过配置databases来修改这一数字，redis启动连接后会自动选择0号数据库，不过可以SELECT命令更换数据库，每个数据都是独立的，类似数据库


数据类型命令：字符串，list、hashmap、set无序集合、有序集合（sorted set）、KEY 相关命令、事务 和 连接相关命令

Redis中支持RDB和AOF这两种持久化机制，目的都是避免因进程退出，造成的数据丢失问题。
RDB持久化：把当前进程数据生成时间点快照（point-in-time snapshot）保存到硬盘的过程，避免数据意外丢失。
AOF持久化：以独立日志的方式记录每次写命令，重启时在重新执行AOF文件中的命令达到恢复数据的目的。


RDB持久化是把当前进程数据生成时间点快照（point-in-time snapshot）保存到硬盘的过程，避免数据意外丢失。
RDB触发机制分为手动触发和自动触发。

手动触发的两条命令：
SAVE：阻塞当前Redis服务器，知道RDB过程完成为止。
BGSAVE：Redis 进程执行fork()操作创建出一个子进程，在后台完成RDB持久化的过程。（主流）

自动触发的配置：
save 900 1 //服务器在900秒之内，对数据库执行了至少1次修改 
save 300 10 //服务器在300秒之内，对数据库执行了至少10修改 
save 60 1000 //服务器在60秒之内，对数据库执行了至少1000修改 
// 满足以上三个条件中的任意一个，则自动触发 BGSAVE 操作 
// 或者使用命令CONFIG SET 命令配置 


AOF的使用：在redis.conf配置文件中，将appendonly设置为yes，默认的为no。

命令写入缓冲区，命令问什么先写入缓冲区
由于Redis是单线程响应命令，所以每次写AOF文件都直接追加到硬盘中，那么写入的性能完全取决于硬盘的负载，所以Redis会将命令写入到缓冲区中，然后执行文件同步操作，再将缓冲区内容同步到磁盘中，这样就很好的保持了高性能。

缓冲区同步到文件
既然缓冲区提供了高性能的保障，那么缓冲区中的数据安全问题如何解决呢？只要数据存在于缓冲区，那么就有丢失的危险。那么，如果控制同步的频率呢？Redis中给出了3中缓冲区同步文件的策略。
虽然Redis提供了三种同步策略，兼顾安全和性能的同步策略是：AOF_FSYNC_EVERYSEC。但是仍有丢失数据的风险，而且不是一秒而是两秒的数据

可配置值	说明
AOF_FSYNC_ALWAYS	命令写入aof_buf后调用系统fsync和操作同步到AOF文件，fsync完成后进程程返回
AOF_FSYNC_EVERYSEC	命令写入aof_buf后调用系统write操作，write完成后线程返回。fsync同步文件操作由进程每秒调用一次
AOF_FSYNC_NO	命令写入aof_buf后调用系统write操作，不对AOF文件做fsync同步，同步硬盘由操作由操作系统负责

我们来了解一下，write和fsync操作，在系统中都做了哪些事：
1、write操作：会触发延迟写（delayed write）机制。Linux在内核提供页缓冲区用来提高IO性能，因此，write操作在将数据写入操作系统的缓冲区后就直接返回，而不一定触发同步到磁盘的操作。只有在页空间写满，或者达到特定的时间周期，才会同步到磁盘。因此单纯的write操作也是有数据丢失的风险。
2、fsync操作：针对单个文件操作，做强制硬盘同步，fsync将阻塞直到写入硬盘完成后返回。


重写机制：当一个数据库的命令非常多时，AOF文件就会非常大，为了解决这个问题，Redis引入了AOF重写机制来压缩文件的体积。


触发机制
手动触发：BGREWRITEAOF 命令。
自动触发：根据redis.conf的两个参数确定触发的时机。 
auto-aof-rewrite-percentage 100：当前AOF的文件空间(aof_current_size)和上一次重写后AOF文件空间(aof_base_size)的比值。
auto-aof-rewrite-min-size 64mb：表示运行AOF重写时文件最小的体积。
自动触发时机 = (aof_current_size > auto-aof-rewrite-min-size && (aof_current_size - aof_base_size) / aof_base_size >= auto-aof-rewrite-percentage)



Redis 复制(Replication)
1. 复制介绍
分布式数据库为了获取更大的存储容量和更高的并发访问量，会将原来集中式数据库中的数据分散存储到多个通过网络连接的数据存储节点上。Redis为了解决单点数据库问题，会把数据复制多个副本部署到其他节点上，通过复制，实现Redis的高可用性，实现对数据的冗余备份，保证数据和服务的高度可靠性。

2. 复制的建立
建立复制的配置方式有三种。
1、在redis.conf文件中配置slaveof <masterip> <masterport>选项，然后指定该配置文件启动Redis生效。
2、在redis-server启动命令后加上--slaveof <masterip> <masterport>启动生效。
3、直接使用 slaveof <masterip> <masterport>命令在从节点执行生效。


Redis的复制拓扑结构支持单层或多层复制关系，从节点还可以作为其他从节点的主节点进行复制。
根据拓扑关系可以分为三种：
一主一从
一主多从（主节点有多个从节点）
树型主从结构（从节点还有子从节点）




主从复制的问题
Redis主从复制可将主节点数据同步给从节点，从节点此时有两个作用：

一旦主节点宕机，从节点作为主节点的备份可以随时顶上来。
扩展主节点的读能力，分担主节点读压力。
但是问题来了：
1、一旦主节点宕机，从节点晋升成主节点，同时需要修改应用方的主节点地址，还需要命令所有从节点去复制新的主节点，整个过程需要人工干预。
2、主节点的写能力受到单机的限制。
3、主节点的存储能力受到单机的限制。
第一个问题，我们接下来讲的Sentinel就可以解决。而后两个问题，Redis也给出了方案Redis Cluster。


Redis Sentinel的高可用
Redis Sentinel是一个分布式架构，包含若干个Sentinel节点和Redis数据节点，每个Sentinel节点会对数据节点和其余Sentinel节点进行监控，当发现节点不可达时，会对节点做下线标识。

如果被标识的是主节点，他还会选择和其他Sentinel节点进行“协商”，当大多数的Sentinel节点都认为主节点不可达时，他们会选举出一个Sentinel节点来完成自动故障转移工作，同时将这个变化通知给Redis应用方。

整个过程完全自动，不需要人工介入，所以可以很好解决Redis的高可用问题。


从上面的逻辑架构和故障转移试验中，可以看出Redis Sentinel的以下几个功能。
1、监控：Sentinel节点会定期检测Redis数据节点和其余Sentinel节点是否可达。
2、通知：Sentinel节点会将故障转移通知给应用方。
3、主节点故障转移：实现从节点晋升为主节点并维护后续正确的主从关系。
4、配置提供者：在Redis Sentinel结构中，客户端在初始化的时候连接的是Sentinel节点集合，从中获取主节点信息。
--------------------- 
作者：men_wen 
来源：CSDN 
原文：https://blog.csdn.net/men_wen/article/details/72724406 
版权声明：本文为博主原创文章，转载请附上博文链接！













redis提供6种数据淘汰策略：

volatile-lru：从已设置过期时间的数据集（server.db[i].expires）中挑选最近最少使用的数据淘汰
volatile-ttl：从已设置过期时间的数据集（server.db[i].expires）中挑选将要过期的数据淘汰
volatile-random：从已设置过期时间的数据集（server.db[i].expires）中任意选择数据淘汰
allkeys-lru：从数据集（server.db[i].dict）中挑选最近最少使用的数据淘汰
allkeys-random：从数据集（server.db[i].dict）中任意选择数据淘汰
no-enviction（驱逐）：禁止驱逐数据
 注意这里的6种机制，volatile和allkeys规定了是对已设置过期时间的数据集淘汰数据还是从全部数据集淘汰数据，后面的lru、ttl以及random是三种不同的淘汰策略，再加上一种no-enviction永不回收的策略。

使用策略规则：
1、如果数据呈现幂律分布，也就是一部分数据访问频率高，一部分数据访问频率低，则使用allkeys-lru
 2、如果数据呈现平等分布，也就是所有的数据访问频率都相同，则使用allkeys-random

三种数据淘汰策略：
 ttl和random比较容易理解，实现也会比较简单。主要是Lru最近最少使用淘汰策略，设计上会对key 按失效时间排序，然后取最先失效的key进行淘汰
 
 
 
 Redis服务端处理流程：
 
请求重定向
在集群模式下，Redis接收任何键相关命令时首先计算键对应的槽，再根据槽找出所对应的节点，如果节点是自身，则处理键命令；否则回复MOVED重定向错误，通知客户端请求正确的节点。这个过程称为MOVED重定向
 
 使用redis-cli命令时，可以加入-c参数支持自动重定向，简化手动发起重定向操作
 
 redis-cli自动帮我们连接到正确的节点执行命令， 这个过程是在redis-cli内部维护，实质上是client端接到MOVED信息之后再次发起请求，并不在Redis节点中完成请求转发，如图10-30所示。
 节点对于不属于它的键命令只回复重定向响应，并不负责转发。熟悉Cassandra的用户希望在这里做好区分，不要混淆。正因为集群模式下把解析发起重定向的过程放到客户端完成，所以集群客户端协议相对于单机有了很大的变化。
 
 
 键命令执行步骤主要分两步：计算槽，查找槽所对应的节点。下面分别介绍。
 1.计算槽
 Redis首先需要计算键所对应的槽。根据键的有效部分使用CRC16函数计算出散列值，再取对16383的余数，使每个键都可以映射到0~16383槽范围内。
 
 2.槽节点查找
 Redis计算得到键对应的槽后，需要查找槽所对应的节点。集群内通过消息交换每个节点都会知道所有节点的槽信息，内部保存在clusterState结构中，结构所示：
 typedef struct clusterState {
     clusterNode *myself; /* 自身节点,clusterNode代表节点结构体 */
     clusterNode *slots[CLUSTER_SLOTS]; /* 16384个槽和节点映射数组， 数组下标代表对应的槽 */
     ...
 } clusterState;
 
 
 客户端ASK重定向流程
 Redis集群支持在线迁移槽（slot）和数据来完成水平伸缩，当slot对应的数据从源节点到目标节点迁移过程中，客户端需要做到智能识别，保证键命令可正常执行。例如当一个slot数据从源节点迁移到目标节点时，期间可能出现一部分数据在源节点，而另一部分在目标节点，如图10-32所示。
 当出现上述情况时，客户端键命令执行流程将发生变化，如下所示：
 1）客户端根据本地slots缓存发送命令到源节点，如果存在键对象则直接执行并返回结果给客户端。
 2）如果键对象不存在，则可能存在于目标节点，这时源节点会回复ASK重定向异常。格式如下：（error）ASK{slot}{targetIP}： {targetPort}。
 3）客户端从ASK重定向异常提取出目标节点信息，发送asking命令到目标节点打开客户端连接标识，再执行键命令。如果存在则执行，不存在则返回不存在信息。
 
 
 ASK与MOVED虽然都是对客户端的重定向控制，但是有着本质区别。ASK重定向说明集群正在进行slot数据迁移，客户端无法知道什么时候迁移完成，因此只能是临时性的重定向，客户端不会更新slots缓存。但是MOVED重定向说明键对应的槽已经明确指定到新的节点，因此需要更新slots缓存。
 




MOVED重定向
Redis客户端可以向集群的任意一个节点发送查询请求，节点接收到请求后会对其进行解析，如果是操作单个key的命令或者是包含多个在相同槽位key的命令，那么该节点就会去查找这个key是属于哪个槽位的。
如果key所属的槽位由该节点提供服务，那么就直接返回结果。否则就会返回一个MOVED错误：
GET x
-MOVED 3999 127.0.0.1:6381
复制代码这个错误包括了对应的key属于哪个槽位（3999）以及该槽位所在的节点的IP地址和端口号。client收到这个错误信息后，就将这些信息存储起来以便可以更准确的找到正确的节点。

当客户端收到MOVED错误后，可以使用CLUSTER NODES或CLUSTER SLOTS命令来更新整个集群的信息，因为当重定向发生时，很少会是单个槽位的变更，一般都会是多个槽位一起更新。因此，在收到MOVED错误时，客户端应该尽早更新集群的分布信息。当集群达到稳定状态时，客户端保存的槽位和节点的对应信息都是正确的，cluster的性能也会达到非常高效的状态。

除了MOVED重定向之外，一个完整的集群还应该支持ASK重定向。

 
 https://blog.csdn.net/HoldBelief/article/details/79796558
 http://blog.jobbole.com/102194/
 
 
 
 

 http://blog.huangz.me/diary/2016/redis-count-online-users.html
 使用 Redis 统计在线用户人数：
方案 1 ：使用有序集合
方案 2 ：使用集合
方案 3 ：使用 HyperLogLog
方案 4 ：使用位图（bitmap）



---------------------------------------------------------------------------------------------------------------------



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


主从带Sentinel哨兵模式

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


---------------------------------------------------------------------------------------------------------------------
https://www.jianshu.com/p/3bc684502f20
http://www.importnew.com/26921.html
https://blog.csdn.net/u011489043/article/details/78922390
https://www.biaodianfu.com/redis-vs-memcached.html

Redis和Memcached区别：
1、支持数据类型、
2、持久化
3、数据备份恢复
4、路由规则、
5、网络IO模型：memcached是多线程，非阻塞IO复用的网络模型，redis使用单线程的IO复用模型

1、Redis支持服务器端的数据操作：Redis相比Memcached来说，拥有更多的数据结构和并支持更丰富的数据操作
2、内存使用效率对比：使用简单的key-value存储的话，Memcached的内存利用率更高，而如果Redis采用hash结构来做key-value存储，由于其组合式的压缩，其内存利用率会高于Memcached
3、性能对比：由于Redis只使用单核，而Memcached可以使用多核，所以平均每一个核上Redis在存储小数据时比Memcached性能更高。


---------------------------------------------------------------------------------------------------------------------

Redis：解决分布式高并发修改同一个Key的问题
如何解决Redis的并发竞争key问题
 
 https://www.cnblogs.com/yy3b2007com/p/9383713.html
 https://blog.csdn.net/jason1993as/article/details/86850772
 
 1、Redis事务：MULTI + 命令 + EXEC
 2、分布式锁：
 
 
1、Redis事务：MULTI + 命令 + EXEC
  采用使用mutil+watch实现
this.jedis.watch("lock_test");
Transaction tx = this.jedis.multi();
tx.hmset("lock_test", newValues);
List<Object> exec = tx.exec();
 
 
 2、分布式锁的实现方式：
 数据库
    Memcached（add命令）
    Redis（setnx命令）
    Zookeeper（临时节点）
 
 
 通过Jedis的setnx、multi事务及watch实现三种分布式跨JVM锁的方法代码示例
 http://www.xwood.net/_site_domain_/_root/5870/5874/t_c267546.html
 
---------------------------------------------------------------------------------------------------------------------
 redis如何解决key冲突
1、业务隔离

不同的业务使用不同的redis集群，或者协议使用redis的不同db。

2、良好的Redis Key的设计

格式：业务标识：系统名称：模块名称：关键词简写

比如：保险：用户管理：用户申请：手机号

Redis Key：bx:um:reg:mobile
 
 
 
 
 ---------------------------------------------------------------------------------------------------------------------
 Redis的五种数据类型的实现是什么数据结构
 
Redis的五种数据结构如下：
 String：字符串，其本质是个 byte 数组,可以包含任何数据
 Hash：字典，二维结构：数组+链表，第一维度:数组，第二维度:链表，哈希表作为字典的底层实现，哈希表使用链表来解决键冲突问题
 List：列表，链表（双向链表）的实现
 Set：集合，通过 hash table 实现的
 Sorted Set：有序集合，Hash+跳跃表，在set的基础上增加了一个标识属性，它可以在set添加或修改元素时指定，每次指定，set会自动按标识调整顺序
 
 
 一、字符串
 Redis使用C语言编写，但是Redis的字符串并没有直接使用C语言传统的字符串，而是自己构建了一种名为简单动态字符串的抽象类型SDS，其本质是个 byte 数组,可以包含任何数据，是二进制安全的。
 
 二、字典
 Redis使用哈希表作为字典的底层实现，每个字典都有两个哈希表，一个平时使用，另一个仅在进行rehash时使用
 哈希表使用链表来解决键冲突问题，被分配到同一个索引上的多个键值对会连接成一个单向链表
 
 三、列表
 Redis构建了自己的链表的实现，其特性如下：
 1、双端：链表节点提供有prev和next对象,获取某个节点的前置节点和下一个节点的速度为O(1).
 2、无环：表头节点prev对象和表尾节点next对象都指向NULL,链表的访问都是以NULL访问为终点.
 3、带有表头和表尾对象：通过list结构的head和tail，获取表头和表尾对象的速度为O(1).
 4、带有长度计数器：获取链表长度的直接读取len字段值.速度为O(1).
 5、多态：通过dup、free、match三个方法,实现链表的多态,保存不同类型的值

四、集合
Redis 集合是 string 类型的无序集合。set 元素最大可以包含(2 的 32 次方)个元素。set 是通过 hash table 实现的，hash table 会随着添加或者删除自动的调整大小。调整 hash table 大小时候需要同步(获取写锁)会阻塞其他读写操作。

五、有序集合
有序集合(sorted set) 在set的基础上增加了一个标识属性，它可以在set添加或修改元素时指定，每次指定，set会自动按标识调整顺序，set的每一个元素都会关联一个double类型的score。使用时往往我们把要排序的字段作为score存储，对象id则作为元素存储

 
参考  
https://www.jianshu.com/p/2f4609e0ec6e  
https://blog.csdn.net/shengqianfeng/article/details/82684354


---------------------------------------------------------------------------------------------------------------------
Redis主从复制机制：

Redis的主从复制机制是指可以让从服务器(slave)能精确复制主服务器(master)的数据

主从复制的方式和工作原理
Redis的主从复制是异步复制，异步分为两个方面，
一个是master服务器在将数据同步到slave时是异步的，因此master服务器在这里仍然可以接收其他请求，
一个是slave在接收同步数据也是异步的。


复制方式
Redis主从复制分为以下三种方式：
一、当master服务器与slave服务器正常连接时，master服务器会发送数据命令流给slave服务器,将自身数据的改变复制到slave服务器。
二、当因为各种原因master服务器与slave服务器断开后，slave服务器在重新连上master服务器时会尝试重新获取断开后未同步的数据即部分同步，或者称为部分复制。
三、如果无法部分同步(比如初次同步)，则会请求进行全量同步，这时master服务器会将自己的rdb文件发送给slave服务器进行数据同步，并记录同步期间的其他写入，再发送给slave服务器，以达到完全同步的目的，这种方式称为全量复制。


主从复制的作用
1、保存Redis数据副本
2、读写分离
3、高可用性与故障转移



参考
https://juejin.im/post/5d14bb8ff265da1b602915c5
https://zhuanlan.zhihu.com/p/60239657


---------------------------------------------------------------------------------------------------------------------




