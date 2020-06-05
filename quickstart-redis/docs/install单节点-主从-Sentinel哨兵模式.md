部署模式：
1、单节点模式
2、单节点带有从节点模式（一主一从，一主多从， 树型主从结构（从节点还有子从节点））
3、主从带Sentinel哨兵模式
4、集群模式



参考
https://cloud.tencent.com/developer/news/8009
http://www.cnblogs.com/yiwangzhibujian/p/7047458.html
https://www.cnblogs.com/yu421/p/8081544.html




Redis默认端口6379

Linux 下安装
下载地址：http://redis.io/download，下载最新文档版本。

本教程使用的最新文档版本为 2.8.17，下载并安装：

$ wget http://download.redis.io/releases/redis-2.8.17.tar.gz
$ tar xzf redis-2.8.17.tar.gz
$ cd redis-2.8.17
$ make
make完后 redis-2.8.17目录下会出现编译后的redis服务程序redis-server,还有用于测试的客户端程序redis-cli,两个程序位于安装目录 src 目录下：

下面启动redis服务.

$ cd src
$ ./redis-server
注意这种方式启动redis 使用的是默认配置。也可以通过启动参数告诉redis使用指定配置文件使用下面命令启动。

$ cd src
$ ./redis-server redis.conf
redis.conf是一个默认的配置文件。我们可以根据需要使用自己的配置文件。

启动redis服务进程后，就可以使用测试客户端程序redis-cli和redis服务交互了。 比如：

$ cd src
$ ./redis-cli
redis> set foo bar
OK
redis> get foo
"bar"




Mac 下安装
 1. 官网http://redis.io/ 下载最新的稳定版本,这里是3.2.0
 2. sudo mv 到 /usr/local/
 3. sudo tar -zxf redis-3.2.0.tar 解压文件
 4. 进入解压后的目录 cd redis-3.2.0
 5. sudo make test 测试编译
 6. sudo make install 
 
 
启动：
redis-server

 
 mac 下安装也可以使用 homebrew，homebrew 是 mac 的包管理器。
1、执行 brew install redis
2、启动 redis，可以使用后台服务启动 brew services start redis。或者直接启动：redis-server /usr/local/etc/redis.conf
 
 
 
 https://blog.csdn.net/men_wen/article/details/72590550
 建立复制的配置方式有三种。
 1、在redis.conf文件中配置slaveof <masterip> <masterport>选项，然后指定该配置文件启动Redis生效。
 2、在redis-server启动命令后加上--slaveof <masterip> <masterport>启动生效。
 3、直接使用 slaveof <masterip> <masterport>命令在从节点执行生效。
 
 
 Redis的复制拓扑结构支持单层或多层复制关系，从节点还可以作为其他从节点的主节点进行复制。
 根据拓扑关系可以分为三种：
 一主一从
 一主多从（主节点有多个从节点）
 树型主从结构（从节点还有子从节点）
 
 
 
 Redis主从复制可将主节点数据同步给从节点，从节点此时有两个作用：
 1、一旦主节点宕机，从节点作为主节点的备份可以随时顶上来。
 2、扩展主节点的读能力，分担主节点读压力。
 
 
 主从带Sentinel哨兵模式部署
 https://blog.csdn.net/men_wen/article/details/72724406
 https://blog.csdn.net/yan7895566/article/details/79157558
 
 
 主从带Sentinel哨兵模式启动（两种方法） 
 redis-sentinel sentinel-26379.conf
 redis-server sentinel-26379.conf --sentinel
 
 
 可以看出Redis Sentinel的以下几个功能。
 1、监控：Sentinel节点会定期检测Redis数据节点和其余Sentinel节点是否可达。
 2、通知：Sentinel节点会将故障转移通知给应用方。
 3、主节点故障转移：实现从节点晋升为主节点并维护后续正确的主从关系。
 4、配置提供者：在Redis Sentinel结构中，客户端在初始化的时候连接的是Sentinel节点集合，从中获取主节点信息。
 
 
 当主节点挂掉时候，此时，Redis Sentinel对主节点进行客观下线（Objectively Down， 简称 ODOWN）的判断，确认主节点不可达，则通知从节点中止复制主节点的操作。
 当主节点下线时长超过配置的下线时长30000秒，Redis Sentinel执行故障转移操作。
 重新选择一个节点作为主节点，断开旧主节点的连接，端口为6380的Redis数据节点成为新的主节点，端口为6379的旧主节点断开连接
 重启挂掉的旧主节点，他被降级成为新主节点的从节点。
           

 
 
 
 
 
 
 
 
 