redis是一个开源的，C语言编写、bsd协议的，键值对的缓存和存储的、通常被作为NoSql的内存数据库  
   键值包含--字符串、列表、集合、有序集合、散列表、位图、hyperloglogs  
   
随便连接一个节点，进行set的时候，会转到存储对应的key的主节点来处理


  
  set key value  -->设置键值对  
  get key           --获取键对应的值  
 getset key field--设置新的键值 并返回key的旧值  
Hget key field  
  
 del   key       --删除key和关联值  
Hdel key value  --删除删列表的字段的值  
  
 incr key  --以key所对应的值的数字值加一  
decr key  
incrby key num  
decrby key num   --如果key不存在，或者key所对应的值的类型不正确  则将key所对应的值置为0 再进行操作  
  
Exists  key  --判断key是否存在  
Hexists key field  --判断key或者字段是否存在  
  
help  
help del --查看命令帮助  
expire key seconds   --设置key的过期时间  
quit   --退出redis的客户端  
  
  
Hget key field   --获取hash字段的值  

主要命令：源文档 <http://try.redis.io/>   
DECR, DECRBY, DEL, EXISTS, EXPIRE, GET, GETSET, HDEL, HEXISTS, HGET, HGETALL, HINCRBY, HKEYS, HLEN, HMGET, HMSET, HSET, HVALS, INCR, INCRBY, KEYS, LINDEX, LLEN, LPOP, LPUSH, LRANGE, LREM, LSET, LTRIM, MGET, MSET, MSETNX, MULTI, PEXPIRE, RENAME, RENAMENX, RPOP, RPOPLPUSH, RPUSH, SADD, SCARD, SDIFF, SDIFFSTORE, SET, SETEX, SETNX, SINTER, SINTERSTORE, SISMEMBER, SMEMBERS, SMOVE, SORT, SPOP, SRANDMEMBER, SREM, SUNION, SUNIONSTORE, TTL, TYPE, ZADD, ZCARD, ZCOUNT, ZINCRBY, ZRANGE, ZRANGEBYSCORE, ZRANK, ZREM, ZREMRANGEBYSCORE, ZREVRANGE, ZSCORE  

主要命令分为5类
string类型   --例如 get set
hash类型    --hget hgetall
list类型      -- lrange
set类型     --smembers
sortedset类型  ---zremrangebyscore

命令的返回值  
      --状态返回--status reply--OK  
     --错误返回--error reply--(error)some   
    --整数返回--integer reply--(integer)1  
    --字符串返回--bulk reply--"1"--(nil)  
   --多行字符串回复--multi bulk reply--1) "bar"  
通配符  
 --* ? [a-z] \.  
一个redis实例 支持16个字典 使用 select 数字 进行切换 0--15  
  
Map  
SortedSet  
Set  
Bucket  
List  
  
相关参考资料
Redis官网--http://redis.io/
                 在线测试redis命令 http://try.redis.io/
               Redis文档 https://github.com/antirez/redis-doc
               官方文档 http://redis.io/documentation 
Redis 支持的客户端编程语言  http://redis.io/clients
我是用的是java版本的  Redisson  最新版1.2.2
Redisson源码  https://github.com/mrniko/redisson
Redisson的wiki https://github.com/mrniko/redisson/wiki



redis遍历：keys和scan



















