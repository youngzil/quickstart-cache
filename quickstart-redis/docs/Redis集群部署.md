- [使用Redis的命令部署](#使用Redis的命令部署)
- [使用ruby脚本安装集群](#使用ruby脚本安装集群)



---------------------------------------------------------------------------------------------------------------------
## 使用Redis的命令部署

Redis 数据分区
Redis Cluster采用虚拟槽分区，所有的键根据哈希函数映射到0 ～ 16383，计算公式：slot = CRC16(key)&16383。每一个节点负责维护一部分槽以及槽所映射的键值数据。  
虚拟槽范围0 ～ 16383（共有16384个，也就是2的14次方）  


Redis虚拟槽分区的特定：
1. 解耦数据和节点之间的关系，简化了节点扩容和收缩难度。
2. 节点自身维护槽的映射关系，不需要客户端或者代理服务维护槽分区元数据。
3. 支持节点、槽、键之间的映射查询，用于数据路由、在线伸缩等场景。


Redis集群功能限制  
Redis集群相对单机在功能上有一定限制。  
1. key批量操作支持有限。如：MSET``MGET，目前只支持具有相同slot值的key执行批量操作。
3. key事务操作支持有限。支持多key在同一节点上的事务操作，不支持分布在多个节点的事务功能。
4. key作为数据分区的最小粒度，因此不能将一个大的键值对象映射到不同的节点。如：hash、list。
5. 不支持多数据库空间。单机下Redis支持16个数据库，集群模式下只能使用一个数据库空间，即db 0。
6. 复制结构只支持一层，不支持嵌套树状复制结构。


搭建集群工作分为三步：
1. 准备节点
2. 节点握手：cluster meet <ip> <port>
3. 分配槽：cluster addslots {0..5461}
4. 给节点设置从节点：cluster replicate 29978c0169ecc0a9054de7f4142155c1ab70258b



Redis 集群一般由多个节点组成，节点数量为6个才能保证组成完整高可用的集群。
节点握手是指一批运行在集群模式的节点通过Gossip协议彼此通信，达到感知对方的过程。节点握手是集群彼此通信的第一步，由客户端发起命令：cluster meet <ip> <port>

接下来为节点分配槽空间。通过cluster addslots命令。
redis-cli -h 127.0.0.1 -p 6379 cluster addslots {0..5461}
OK
redis-cli -h 127.0.0.1 -p 6380 cluster addslots {5462..10922}
OK
redis-cli -h 127.0.0.1 -p 6381 cluster addslots {10923..16383}
OK


使用cluster replicate <nodeid>在从节点上执行。
redis-cli -h 127.0.0.1 -p 6382 cluster replicate 29978c0169ecc0a9054de7f4142155c1ab70258b
OK
redis-cli -h 127.0.0.1 -p 6383 cluster replicate 8f285670923d4f1c599ecc93367c95a30fb8bf34
OK
redis-cli -h 127.0.0.1 -p 6384 cluster replicate 66478bda726ae6ba4e8fb55034d8e5e5804223ff
OK
--------------------- 
作者：men_wen 
来源：CSDN 
原文：https://blog.csdn.net/men_wen/article/details/72853078 
版权声明：本文为博主原创文章，转载请附上博文链接！


---------------------------------------------------------------------------------------------------------------------
## 使用ruby脚本安装集群

yum install ruby //安装ruby
yum install rubygems  //安装rubygems，最新版本会自动安装
gem install redis


ruby redis-trib.rb  create --replicas 1 192.168.127.130:7000 192.168.127.130:7001 192.168.127.130:7002 192.168.127.130:7003 192.168.127.130:7004 192.168.127.130:7005 

每个Redis的节点都有一个ID值，此ID将被此特定redis实例永久使用，以便实例在集群上下文中具有唯一的名称。 每个节点都会记住使用此ID的每个其他节点，而不是通过IP或端口。IP地址和端口可能会发生变化，但唯一的节点标识符在节点的整个生命周期内都不会改变。 我们简单地称这个标识符为节点ID。


集群扩容
1、集群集群：ruby redis-trib.rb add-node 192.168.127.130:7006 192.168.127.130:7000
2、重新分配槽：ruby redis-trib.rb reshard 192.168.127.130:7000
3、把从节点添加到集群，然后设置从节点，cluster replicate 71ecd970838e9b400a2a6a15cd30a94ab96203bf(主节点的ID，这里是7006)


收缩集群删除节点：
1、删除从节点：ruby redis-trib.rb del-node 192.168.127.130:7007 991ed242102aaa08873eb9404a18e0618a4e37bd
2、把要删除的Master主节点的数据槽移动到其他Master主节点上，以免数据丢失。
ruby redis-trib.rb reshard 192.168.127.130:7006
创建输入200，这里要输入199，因为计数是从0开始的，切记。
3、删除master主节点：ruby redis-trib.rb del-node 192.168.127.130:7006 71ecd970838e9b400a2a6a15cd30a94ab96203bf



集群的扩容：
1、准备新节点
2、加入集群
3、迁移槽和数据：
  1、目标6385节点中，将槽6918设置为导入状态
  2、源6380节点中，将槽6918设置为导出状态
  3、执行migrate命令进行迁移 keys
  4、向任意节点发送CLUSTER SETSLOT <slot> NODE <target_name>命令，将槽指派的信息发送给节点，然后这个节点会将这个指派信息发送至整个集群。
  5、其实已经结束了，还可以再给新节点添加从节点，使用cluster replicate <master_id>命令为主节点添加从节点，集群模式下不支持slaveof命令。
  
  
收缩集群：删除的顺序是先删除Slave从节点，然后在删除Master主节点，
收缩集群以为着缩减规模，需要从集群中安全下线部分节点。需要考虑两种情况：
1、确定下线的节点是否有负责槽，如果是，需要把槽迁移到其他节点，保证节点下线后整个槽节点映射的完整性。
2、当下线节点不在负责槽或着本身是从节点时，就可以通知集群内其他节点忘记下线节点，当所有节点忘记该节点后就可以正常关闭。



参考
https://segmentfault.com/a/1190000023083072
https://cloud.tencent.com/developer/article/1644783
https://www.cnblogs.com/guarderming/p/13237649.html



---------------------------------------------------------------------------------------------------------------------




集群部署参考：redis集群搭建及扩容和缩容操作
https://blog.csdn.net/qq_20679251/article/details/81194522
https://blog.csdn.net/zsj777/article/category/7646713
https://blog.csdn.net/zsj777/article/details/80235568
https://www.jianshu.com/p/22cc38569726



