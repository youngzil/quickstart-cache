#!/bin/bash

export BASE_PATH=`cd $(dirname -- $0); pwd`

#创建文件夹
cd $BASE_PATH
mkdir -p /root/yangzl/700{6..7}

# 拷贝配置文件
cp redis.conf 7006
cp redis.conf 7007

# 修改配置文件内容
sed -i 's#7000#7006#g' 7006/redis.conf
sed -i 's#7000#7007#g' 7007/redis.conf

#启动新节点实例：
redis-6.0.6/src/redis-server 7006/redis.conf
redis-6.0.6/src/redis-server 7007/redis.conf

#查看进程：
ps -ef | grep redis-server

#添加主节点：（7000实例是管理节点）
#'把7006实例添加到7000实例这个主节点所在集群内(此时已经有了4个主节点)
redis-6.0.6/src/redis-cli --cluster add-node 127.0.0.1:7006 127.0.0.1:7000

#查看节点状态
redis-6.0.6/src/redis-cli -p 7000 cluster nodes


#转移slot（重新分片）：
#'操作集群管理节点从新分配，并在交互界面指定分片大小、选择接收分片的节点ID
redis-6.0.6/src/redis-cli --cluster reshard 127.0.0.1:7000

#How many slots do you want to move (from 1 to 16384)?4096
#通过人工手动计算数据分片总大小除以主节点后的数字

#What is the receiving node ID?2129d28f0a86fc89571e49a59a0739812cff7953
#选择接收数据分片的节点ID，（这是新增节点7006实例的ID号）

#Source node #1: all
#选择从哪些源主节点重新分片给新主节点）（all是所有节点）

#Do you want to proceed with the proposed reshard plan (yes/no)?yes
#确认修改以上的操作

#重新查看主节点状态：（可以看到集群数据的重新分片）
redis-6.0.6/src/redis-cli -p 7000 cluster nodes



#添加从节点：
#'把7007实例节点添加到7006实例主节点内，并指定对应7006实例主节点坐在集群的管理节点
master_id=`redis-6.0.6/src/redis-cli -p 7000 cluster nodes | grep master | grep 7006 | awk '{print $1}'`
echo $master_id
redis-6.0.6/src/redis-cli --cluster add-node 127.0.0.1:7007 127.0.0.1:7000 --cluster-slave --cluster-master-id $master_id


#查看从节点状态：（可以看到主备信息）
redis-6.0.6/src/redis-cli -p 7000 cluster nodes


