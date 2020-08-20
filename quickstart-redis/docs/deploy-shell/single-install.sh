#!/bin/bash

#export BASE_PATH=`cd $(dirname -- $0)/..; pwd`
export BASE_PATH=`cd $(dirname -- $0); pwd`
#export BASE_PATH=`pwd`
export REDIS_PATH=/root/yangzl

#编译Redis
cd $BASE_PATH
tar -xzvf redis-6.0.6.tar.gz
cd redis-6.0.6
make && make install

#创建文件夹
cd $BASE_PATH
mkdir -p $REDIS_PATH/700{0..5}

# 拷贝配置文件
cp redis.conf 7000
cp redis.conf 7001
cp redis.conf 7002
cp redis.conf 7003
cp redis.conf 7004
cp redis.conf 7005

# 修改配置文件内容
sed -i 's#7000#7001#g' 7001/redis.conf
sed -i 's#7000#7002#g' 7002/redis.conf
sed -i 's#7000#7003#g' 7003/redis.conf
sed -i 's#7000#7004#g' 7004/redis.conf
sed -i 's#7000#7005#g' 7005/redis.conf

#启动
redis-6.0.6/src/redis-server 7000/redis.conf
redis-6.0.6/src/redis-server 7001/redis.conf
redis-6.0.6/src/redis-server 7002/redis.conf
redis-6.0.6/src/redis-server 7003/redis.conf
redis-6.0.6/src/redis-server 7004/redis.conf
redis-6.0.6/src/redis-server 7005/redis.conf


#查看
ps -ef|grep redis

# --replicas 1",1是代表每一个主有一个从,后面的是所有节点的地址与端口信息
redis-6.0.6/src/redis-cli --cluster create --cluster-replicas 1 20.26.85.227:7000 20.26.85.227:7001 20.26.85.227:7002 20.26.85.227:7003 20.26.85.227:7004 20.26.85.227:7005

#查看节点状态
redis-6.0.6/src/redis-cli -c -h 20.26.85.227 -p 7000 cluster nodes
#bin/redis-cli -c -h 20.26.37.179 -p 28001 -a 密码 cluster nodes
#bin/redis-cli -c -h 20.26.37.179 -p 28001 -a cmVkaXM= cluster nodes
#bin/redis-cli -c -h 20.26.37.180 -p 28003 -a cmVkaXM= cluster nodes

#停止redis命令
#redis-6.0.6/src/redis-cli -c -h 20.26.85.227 -p 7000 shutdown

