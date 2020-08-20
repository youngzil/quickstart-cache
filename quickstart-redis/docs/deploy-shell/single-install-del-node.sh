#!/bin/bash

export BASE_PATH=`cd $(dirname -- $0); pwd`

cd $BASE_PATH

#查看从节点状态：（可以看到主备信息）
redis-5.0.8/src/redis-cli -p 7000 cluster nodes



#移动要删除节点的数据分片：第一次

#'操作集群管理节点从新分配，并在交互界面指定分片大小、选择接收分片的节点ID
redis-6.0.6/src/redis-cli --cluster reshard 127.0.0.1:7000

#方法是根据要删除master节点的分片位置，然后一个组分一个节点 ， 也可以直接移动所有数据片到一个节点

#How many slots do you want to move (from 1 to 16384)? 1365                    
#通过人工手动查看数据分片总大小

#What is the receiving node ID? e64f9074a3733fff7baa9a4848190e56831d5447
#选择接收数据分片的节点ID

#Source node #1: 2129d28f0a86fc89571e49a59a0739812cff7953
#选择从哪些源主节点重新分片给新主节点（这是要删除的主节点的ID号）

# Source node #2:done
#这是结束命令

#Do you want to proceed with the proposed reshard plan (yes/no)? yes           
#确认修改以上的操作

#查看从节点状态：（可以看到主备信息）
redis-6.0.6/src/redis-cli -p 7000 cluster nodes



#继续移动数据片：第二次

#'操作集群管理节点从新分配，并在交互界面指定分片大小、选择接收分片的节点ID
redis-6.0.6/src/redis-cli --cluster reshard 127.0.0.1:7000

# 方法是根据要删除master节点的分片位置，然后一个组分一个节点 ， 也可以直接移动所有数据片到一个节点

#How many slots do you want to move (from 1 to 16384)? 1366                    
#通过人工手动查看数据分片总大小

#What is the receiving node ID? f6c1aaea3a8c56e0c7dee8ad7ae17e26dd04244c
#选择接收数据分片的节点ID，（这是新增节点7006实例的ID号）

#Source node #1: 2129d28f0a86fc89571e49a59a0739812cff7953
#选择从哪些源主节点重新分片给新主节点（这是要删除的主节点的ID号）

#Source node #2:done
#这是结束命令

#Do you want to proceed with the proposed reshard plan (yes/no)? yes           
#确认修改以上的操作

#查看从节点状态：（可以看到主备信息）
redis-6.0.6/src/redis-cli -p 7000 cluster nodes



#最后一次移动数据片：
#'操作集群管理节点从新分配，并在交互界面指定分片大小、选择接收分片的节点ID
redis-6.0.6/src/redis-cli --cluster reshard 127.0.0.1:7000

#方法是根据要删除master节点的分片位置，然后一个组分一个节点 ， 也可以直接移动所有数据片到一个节点
#How many slots do you want to move (from 1 to 16384)? 1365      

#查看从节点状态：（可以看到主备信息）
redis-6.0.6/src/redis-cli -p 7000 cluster nodes




#删除清空数据片的主节点：
#'删除已经清空数据的7006实例
redis-6.0.6/src/redis-cli --cluster del-node 127.0.0.1:7006 39e2074134089c7ea6b589735c25078c6a7f0610
#删除没有主库的7007实例
redis-6.0.6/src/redis-cli --cluster del-node 127.0.0.1:7007 539b42570a46e620ceb1fc7cb830530890a784d5

#查看从节点状态：（可以看到主备信息）
redis-6.0.6/src/redis-cli -p 7000 cluster nodes





#其他配置管理：

#内存信息查看
redis-6.0.6/src/redis-cli -p 7000 -a redhat info memory

#设置最大只能使用100MB的内存
#redis-6.0.6/src/redis-cli -p 7000 -a redhat config set maxmemory 102400000

