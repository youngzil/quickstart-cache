1、Redis启动、连接命令
2、Redis集群批量删除key
   删除当前数据库中的所有Key
3、Redis连接相关的一些基本命令
   Redis服务器相关的一些基本命令。
   Redis基准测试中可用选项的列表。
   与键相关的一些基本命令
   用于在Redis中管理字符串String的基本命令
   与哈希/散列Hash相关的一些基本命令
   与列表List相关的一些基本命令
   与集合Set相关的一些基本命令
4、redis集群客户端命令.md





---------------------------------------------------------------------------------------------------------------------
Redis启动、连接命令

Redis支持五种数据类型：
string（字符串），
hash（哈希），
list（列表），
set（集合）
zset(sorted set：有序集合)。

连接：
本机redis-cli
远程redis-cli -h host -p port -a password
./redis-cli -h 192.168.204.131 -p 6379 -c
redis-cli -c -h 10.76.224.229 -p 6202 -a 密码
redis-cli -c -h 10.1.243.23 -p 7000
redis-cli -c -h 20.26.85.233 -p 6379


redis-cli -c -h 10.76.224.228 -p 6201 -a cmVkaXM=
redis-cli -c -h 10.76.224.229 -p 6202 -a cmVkaXM=
redis-cli -c -h 10.76.224.231 -p 6203 -a cmVkaXM=




redis-cli 
redis-cli -h host -p port -a password
./redis-cli -h 192.168.204.131 -p 6379 -c
redis-cli -c -h 10.76.224.229 -p 6202 -a 密码


redis-cli --raw  防止中文乱码

PING


redis-cli -c -h 20.26.37.179 -p 28001

查看集群节点信息
cluster nodes  


redis集群操作
集群方式登陆：
redis-cli -c -h 192.168.133.130 -p 7000//-c说明以集群的方式登录
任意一个节点都可以创建key，或者查看key（演示）
redis-trib.rb check 192.168.133.130:7000//检测集群状态
cluster nodes//列出节点
cluster info//查看集群信息
cluster meet ip port //添加节点
cluster forget node_id //移除某个节点
cluster replicate node_id//将当前节点设置为指定节点的从
cluster saveconfig//保存配置文件



如果是用apt-get或者yum install安装的redis，可以直接通过下面的命令停止/启动/重启redis
/etc/init.d/redis-server stop 
/etc/init.d/redis-server start 
/etc/init.d/redis-server restart

如果是通过源码安装的redis，则可以通过redis的客户端程序redis-cli的shutdown命令来重启redis
1.redis关闭 
redis-cli -h 127.0.0.1 -p 6379 shutdown

2.redis启动 
redis-server

如果上述方式都没有成功停止redis，则可以使用终极武器 kill -9



测试网络PING

设置密码：
CONFIG get requirepass 
CONFIG set requirepass "yiibai" 
CONFIG get requirepass 

客户端向Redis服务器验证自身，并检查服务器是否正在运行
AUTH "password"
PING

获取有关服务器的所有统计信息和信息info


获取配置
CONFIG SET loglevel "notice"
CONFIG GET loglevel
CONFIG GET *


基准测试命令的基本语法redis-benchmark [option] [option value]
通过调用100000个命令检查Redis：redis-benchmark -n 100000  
Redis基准实用程序中多个选项的使用redis-benchmark -h 127.0.0.1 -p 6379 -t set,lpush -n 100000 -q  

在Redis配置文件(redis.conf)中，有一个名称为maxclients的属性，它描述了可以连接到Redis的客户端的最大数量。
config get maxclients
将客户端的最大数目设置为100000，并启动服务器。
redis-server --maxclients 100000



1.redis命令
1 redis执行了make install后，redis的课执行文件都会自动复制到 /usr/local/bin 目录
2 redis-server        redis服务器
3 redis-cli            redis命令行客户端
4 redis-benchmark        redis性能测试工具
5 redis-check-aof        aof文件修复工具
6 redis-check-dump    rdb文件检查工具


2.停止 redis 命令 
# 停止Redis命令
redis-cli shutdown

只能一个节点一个节点的停止
http://doc.redisfans.com/server/shutdown.html


进来不要强制杀死进程，会丢失数据
#kill -9 10252 10257 10262 10267 10272 10294
#也可执行以下命令来关闭redis进程
#pkill -9 redis



---------------------------------------------------------------------------------------------------------------------

Redis集群批量删除key

1、查看集群信息
redis-cli -c -h 20.26.37.179 -p 28001 -a password
redis-cli -c -h 10.76.227.89 -p 6202 -a cmVkaXM=
cluster nodes

2、删除keys
redis-cli -c -h 20.26.37.179 -p 28001 keys "OAUTH2:TOKEN:*" | xargs -t -n1 redis-cli -c -h 20.26.37.179 -p 28001 del
redis-cli -c -h 20.26.37.180 -p 28003 keys "OAUTH2:TOKEN:*" | xargs -t -n1 redis-cli -c -h 20.26.37.180 -p 28003 del
redis-cli -c -h 20.26.37.181 -p 28005 keys "OAUTH2:TOKEN:*" | xargs -t -n1 redis-cli -c -h 20.26.37.181 -p 28005 del


或者
redis-cli -c -h 20.26.37.179 -p 28001 -n 0 keys "OAUTH2:TOKEN:*" | xargs redis-cli -c -h 20.26.37.179 -p 28001 -n 0 del



删除当前数据库中的所有Key

https://blog.csdn.net/iloveyin/article/details/46813427
删除redis所有KEY

如果要访问 Redis 中特定的数据库，使用下面的命令
//下面的命令指定数据序号为0，即默认数据库
redis-cli -h ip -p port -n 0 keys "*"| xargs redis-cli -h ip -p port -n 0 del


删除所有Key，可以使用Redis的flushdb和flushall命令

//删除当前数据库中的所有Key
flushdb
//删除所有数据库中的key
flushall

注：keys 指令可以进行模糊匹配，但如果 Key 含空格，就匹配不到了，暂时还没发现好的解决办法。



---------------------------------------------------------------------------------------------------------------------

下表列出了与Redis连接相关的一些基本命令。
序号	命令	说明
1	AUTH password	使用给定的密码验证服务器
2	ECHO message	打印给定的字符串信息
3	PING	检查服务器是否正在运行
4	QUIT	关闭当前连接
5	SELECT index	更改当前连接的所选数据库


下表列出了与Redis服务器相关的一些基本命令。
序号	命令	说明
1	BGREWRITEAOF	异步重写仅追加的文件
2	BGSAVE	将数据集异步保存到磁盘
3	CLIENT KILL [ip:port] [ID client-id]	杀死或断开指定的客户端的连接
4	CLIENT LIST	获取到服务器的客户端连接列表
5	CLIENT GETNAME	获取当前连接的名称
6	CLIENT PAUSE timeout	在指定时间内停止处理来自客户端的命令
7	CLIENT SETNAME connection-name	设置当前连接名称
8	CLUSTER SLOTS	获取群集插槽到节点映射的数组
9	COMMAND	获取Redis命令详细信息的数组
10	COMMAND COUNT	获取Redis命令的总数
11	COMMAND GETKEYS	提取键给出一个完整的Redis的命令
12	BGSAVE	将数据集异步保存到磁盘
13	COMMAND INFO command-name [command-name …]	获取特定Redis命令详细信息的数组
14	CONFIG GET parameter	获取配置参数的值
15	CONFIG REWRITE	使用内存中配置来重写配置文件
16	CONFIG SET parameter value	将配置参数设置为给定值
17	CONFIG RESETSTAT	重置由INFO返回的统计信息
18	DBSIZE	返回所选数据库中的键数量
19	DEBUG OBJECT key	获取有关键的调试信息
20	DEBUG SEGFAULT	使服务器崩溃
21	FLUSHALL	从所有数据库中删除所有键
22	FLUSHDB	删除当前数据库中的所有键
23	INFO [section]	获取有关服务器的信息和统计信息
24	LASTSAVE	获取上次成功保存到磁盘的UNIX时间戳
25	MONITOR	监听服务器实时接收的所有请求
26	ROLE	返回实例在复制上下文中的角色
27	SAVE	将数据集同步保存到磁盘
28	SHUTDOWN [NOSAVE] [SAVE]	将数据集同步保存到磁盘，然后关闭服务器
29	SLAVEOF host port	使服务器成为另一个实例的从属，或将其提升作为主服务器
30	SLOWLOG subcommand [argument]	管理Redis慢查询日志
31	SYNC	用于复制的命令
32	TIME	返回当前服务器的时间


以下是Redis基准测试中可用选项的列表。
序号	选项	说明	默认值
1	-h	指定服务器主机名	127.0.0.1
2	-p	指定服务器端口	6379
3	-s	指定服务器套接字	
4	-c	指定并行连接的数量	50
5	-n	指定请求的总数	10000
6	-d	指定SET/GET值的数据大小(以字节为单位)	2
7	-k	1=keep alive, 0=reconnect	1
8	-r	使用SET/GET/INCR的随机键，SADD的随机值	
9	-p	管道<numreq>请求	1
10	-h	指定服务器主机名	
11	-q	强制让Redis安装。 只显示query/sec值	
12	--csv	以CSV格式输出	
13	-l	生成循环，永久运行测试	
14	-t	只运行逗号分隔的测试列表	
15	-I	空闲模式。 只打开N个空闲连接并等待


下表列出了与键相关的一些基本命令。
编号	命令	描述
1	DEL key	此命令删除一个指定键(如果存在)。
2	DUMP key	此命令返回存储在指定键的值的序列化版本。
3	EXISTS key	此命令检查键是否存在。
4	EXPIRE key seconds	设置键在指定时间秒数之后到期/过期。
5	EXPIREAT key timestamp	设置在指定时间戳之后键到期/过期。这里的时间是Unix时间戳格式。
6	PEXPIRE key milliseconds	设置键的到期时间(以毫秒为单位)。
7	PEXPIREAT key milliseconds-timestamp	以Unix时间戳形式来设置键的到期时间(以毫秒为单位)。
8	KEYS pattern	查找与指定模式匹配的所有键。
9	MOVE key db	将键移动到另一个数据库。
10	PERSIST key	删除指定键的过期时间，得永生。
11	PTTL key	获取键的剩余到期时间。
12	RANDOMKEY	从Redis返回一个随机的键。
13	RENAME key newkey	更改键的名称。
14	PTTL key	获取键到期的剩余时间(以毫秒为单位)。
15	RENAMENX key newkey	如果新键不存在，重命名键。
16	TYPE key	返回存储在键中的值的数据类型。


下表列出了一些用于在Redis中管理字符串String的基本命令。
编号	命令	描述说明
1	SET key value	此命令设置指定键的值。
2	GET key	获取指定键的值。
3	GETRANGE key start end	获取存储在键上的字符串的子字符串。
4	GETSET key value	设置键的字符串值并返回其旧值。
5	GETBIT key offset	返回在键处存储的字符串值中偏移处的位值。
6	MGET key1 [key2..]	获取所有给定键的值
7	SETBIT key offset value	存储在键上的字符串值中设置或清除偏移处的位
8	SETEX key seconds value	使用键和到期时间来设置值
9	SETNX key value	设置键的值，仅当键不存在时
10	SETRANGE key offset value	在指定偏移处开始的键处覆盖字符串的一部分
11	STRLEN key	获取存储在键中的值的长度
12	MSET key value [key value …]	为多个键分别设置它们的值
13	MSETNX key value [key value …]	为多个键分别设置它们的值，仅当键不存在时
14	PSETEX key milliseconds value	设置键的值和到期时间(以毫秒为单位)
15	INCR key	将键的整数值增加1
16	INCRBY key increment	将键的整数值按给定的数值增加
17	INCRBYFLOAT key increment	将键的浮点值按给定的数值增加
18	DECR key	将键的整数值减1
19	DECRBY key decrement	按给定数值减少键的整数值
20	APPEND key value	将指定值附加到键


下表列出了与哈希/散列Hash相关的一些基本命令。
序号	命令	说明
1	HDEL key field2 [field2]	删除一个或多个哈希字段。
2	HEXISTS key field	判断是否存在散列字段。
3	HGET key field	获取存储在指定键的哈希字段的值。
4	HGETALL key	获取存储在指定键的哈希中的所有字段和值
5	HINCRBY key field increment	将哈希字段的整数值按给定数字增加
6	HINCRBYFLOAT key field increment	将哈希字段的浮点值按给定数值增加
7	HKEYS key	获取哈希中的所有字段
8	HLEN key	获取散列中的字段数量
9	HMGET key field1 [field2]	获取所有给定哈希字段的值
10	HMSET key field1 value1 [field2 value2 ]	为多个哈希字段分别设置它们的值
11	HSET key field value	设置散列字段的字符串值
12	HSETNX key field value	仅当字段不存在时，才设置散列字段的值
13	HVALS key	获取哈希中的所有值


下表列出了与列表List相关的一些基本命令。
序号	命令	说明
1	BLPOP key1 [key2 ] timeout	删除并获取列表中的第一个元素，或阻塞，直到有一个元素可用
2	BRPOP key1 [key2 ] timeout	删除并获取列表中的最后一个元素，或阻塞，直到有一个元素可用
3	BRPOPLPUSH source destination timeout	从列表中弹出值，将其推送到另一个列表并返回它; 或阻塞，直到一个可用
4	LINDEX key index	通过其索引从列表获取元素
5	LINSERT key BEFORE/AFTER pivot value	在列表中的另一个元素之前或之后插入元素
6	LLEN key	获取列表的长度
7	LPOP key	删除并获取列表中的第一个元素
8	LPUSH key value1 [value2]	将一个或多个值添加到列表
9	LPUSHX key value	仅当列表存在时，才向列表添加值
10	LRANGE key start stop	从列表中获取一系列元素
11	LREM key count value	从列表中删除元素
12	LSET key index value	通过索引在列表中设置元素的值
13	LTRIM key start stop	修剪列表的指定范围
14	RPOP key	删除并获取列表中的最后一个元素
15	RPOPLPUSH source destination	删除列表中的最后一个元素，将其附加到另一个列表并返回
16	RPUSH key value1 [value2]	将一个或多个值附加到列表
17	RPUSHX key value	仅当列表存在时才将值附加到列表


下表列出了与集合Set相关的一些基本命令。
序号	命令	说明
1	SADD key member1 [member2]	将一个或多个成员添加到集合
2	SCARD key	获取集合中的成员数
3	SDIFF key1 [key2]	减去多个集合
4	SDIFFSTORE destination key1 [key2]	减去多个集并将结果集存储在键中
5	SINTER key1 [key2]	相交多个集合
6	SINTERSTORE destination key1 [key2]	交叉多个集合并将结果集存储在键中
7	SISMEMBER key member	判断确定给定值是否是集合的成员
8	SMOVE source destination member	将成员从一个集合移动到另一个集合
9	SPOP key	从集合中删除并返回随机成员
10	SRANDMEMBER key [count]	从集合中获取一个或多个随机成员
11	SREM key member1 [member2]	从集合中删除一个或多个成员
12	SUNION key1 [key2]	添加多个集合
13	SUNIONSTORE destination key1 [key2]	添加多个集并将结果集存储在键中
14	SSCAN key cursor []MATCH pattern [COUNT count]	递增地迭代集合中的元素



---------------------------------------------------------------------------------------------------------------------






