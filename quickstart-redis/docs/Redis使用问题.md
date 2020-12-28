1、Too many redirections

解决：
- （1）查看创建集群时，不要使用127.0.01这种地址，使用实际地址。
- （2）new JedisCluster(jedisClusterNodes(jedisClusterNodes, timeout, maxredirection)时，指定最大重试次数maxredirection,一般集群有几个redis实例，就设置几个



2、CROSSSLOT Keys in request don't hash to the same slot的错误

如错误消息所示，只有所有键都属于同一个插槽，操作才能成功。否则，您将看到此失败消息。即使两个/所有插槽都属于同一节点，也会看到此错误。该检查非常严格，根据代码，所有密钥都应哈希到同一插槽。

mset（Multi-key）、rename等命令，报错：
Redis::CommandError: CROSSSLOT Keys in request don’t hash to the same slot>

原因： Redis cluster对多key操作有限，要求命令中所有的key都属于一个slot，才可以被执行。客户端可以对multi-key命令进行拆分，再发给redis。
另外一个局限是，在slot迁移过程中，multi-key命令特别容易报错(CROSSSLOT Keys in request don’t hash to the same slot)。建议不用multi-key命令。

解决： 在key名中增加{XXXX}，这样redis将仅使用XXXX来计算slot的位置



[redis的使用注意事项和问题总结](https://blog.csdn.net/menghuanzhiming/article/details/78962764)






