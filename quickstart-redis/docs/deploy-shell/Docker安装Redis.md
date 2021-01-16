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


[使用docker安装redis，挂载外部配置和数据](https://blog.csdn.net/woniu211111/article/details/80970560)





