- [手动安装](#手动安装)
- [docker compose安装](#Docker-compose安装)


---------------------------------------------------------------------------------------------------------------------
## 手动安装


## Docker中安装Redis

docker search redis

#该方式默认下载的最新版本，如需要下载指定版本在redis后面跟:版本号
docker pull redis

查看当前下载的redis镜像
docker images


创建redis.conf配置文件
cd ~
mkdir -p redis/conf redis/data
touch redis/conf/redis.conf
vim redis/conf/redis.conf


启动Docker Redis镜像
# -p 主机端口：容器端口      -v 主机目录：容器目录

# 公网记得设置密码和修改端口为不常用端口
docker run -d --privileged=true -p 6379:6379 -v /root/redis/conf/redis.conf:/etc/redis/redis.conf -v /root/redis/data:/data --name redis redis:latest redis-server /etc/redis/redis.conf --appendonly yes


Redis启动命令：  
docker run -d --privileged=true -p 6789:6789 -v /root/redis/conf/redis.conf:/etc/redis/redis.conf -v /root/redis/data:/data --name redis redis:latest redis-server /etc/redis/redis.conf --appendonly yes

进入Redis容器
docker exec -it redis bash

redis 如何查看版本号

查看服务端版本
**二者都可以**
redis-server -v 
redis-server --version

查看客户端版本
**二者都可以**
redis-cli -v 
redis-cli --version


[docker安装redis及常用命令](https://blog.csdn.net/u011191463/article/details/83383404)  
[使用docker安装redis，挂载外部配置和数据](https://blog.csdn.net/woniu211111/article/details/80970560)


---------------------------------------------------------------------------------------------------------------------

## Docker compose安装



[Docker docker-compose安装redis单节点](https://www.jianshu.com/p/95cd92a0fd66)  
[redis 基于 docker-compose](https://www.jianshu.com/p/d942b749889f)  
[]()


### 单节点安装



### 集群安装

使用 docker-compose-cluster.yml 启动6个节点

这里yml中的master/follower没有实际意义,具体的主从分配是redis决定的.

docker-compose成功启动六个redis容器之后,进入任意一个,配置redis-cluster集群.



编排容器
docker-compose up -d

进入容器
docker exec -it container_id -bin/bash

配置redis集群,每个master一个follower
redis-cli --cluster create
192.168.88.80:6379
192.168.88.81:6379
192.168.88.82:6379
192.168.88.83:6379
192.168.88.84:6379
192.168.88.85:6379
--cluster-replicas 1


[docker-compose编排redis集群](https://segmentfault.com/a/1190000039024451)  
[docker-compose 搭建 redis集群](https://www.jianshu.com/p/ce14357cf0b4)  
[使用Docker部署Redis集群-三主三从](https://jasonkayzk.github.io/2020/01/17/%E4%BD%BF%E7%94%A8Docker%E9%83%A8%E7%BD%B2Redis%E9%9B%86%E7%BE%A4-%E4%B8%89%E4%B8%BB%E4%B8%89%E4%BB%8E/)  
[]()  



---------------------------------------------------------------------------------------------------------------------


