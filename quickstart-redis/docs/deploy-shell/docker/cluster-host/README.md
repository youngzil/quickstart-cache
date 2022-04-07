NAT可以通过宿主机访问，host只能在同期内部访问

-----------------------------------------------------------------------------
cluster-nat部署


docker compose -f cluster-nat.yml up -d

查看节点是否正常运行：
docker ps -a

集群创建命令
redis-cli --cluster create 172.20.10.2:6371 172.20.10.2:6372 172.20.10.2:6373 172.20.10.2:6374 172.20.10.2:6375 172.20.10.2:6376 --cluster-replicas 1

验证集群
登录之后可以通过 cluster info 查看集群配置信息，可以通过 cluster nodes 查看节点信息：
redis-cli -c -p 6372
127.0.0.1:6372> cluster nodes

读写测试：
127.0.0.1:6372> set Hello World
127.0.0.1:6371> get Hello
127.0.0.1:6371> set test jump
127.0.0.1:6372> get test


关闭 Master 节点：
docker stop redis-cluster-node-1

redis-cli -c -p 6372
127.0.0.1:6372> cluster nodes


重启 Master 节点：
docker start redis-cluster-node-1

docker ps -a

redis-cli -c -p 6372
127.0.0.1:6372> cluster nodes


读写测试：
127.0.0.1:6372> get Hello
172.20.10.2:6375> get test

原来跳转到6371的现在跳转到6375了


docker compose -f cluster-nat.yml down


-----------------------------------------------------------------------------

cluster-host部署

docker compose -f cluster-host.yml up -d

查看节点是否正常运行：
docker ps -a

docker exec -it redis-cluster-node-1 bash

集群创建命令
redis-cli --cluster create 127.0.0.1:6371 127.0.0.1:6372 127.0.0.1:6373 127.0.0.1:6374 127.0.0.1:6375 127.0.0.1:6376 --cluster-replicas 1
# redis-cli -a 1234 redis-cli --cluster create 127.0.0.1:6371 127.0.0.1:6372 127.0.0.1:6373 127.0.0.1:6374 127.0.0.1:6375 127.0.0.1:6376 --cluster-replicas 1

验证集群
redis-cli -c -p 6372
127.0.0.1:6372> cluster nodes

读写测试：
127.0.0.1:6372> set Hello World
127.0.0.1:6371> get Hello
127.0.0.1:6371> set test jump
127.0.0.1:6372> get test

关闭 Master 节点：
docker stop redis-cluster-node-1

docker exec -it redis-cluster-node-2 bash

登录其他节点并查询节点信息：
redis-cli -c -p 6372
127.0.0.1:6372> cluster nodes

-----------------------------------------------------------------------------




参考
[【Redis】docker compose 部署集群模式](https://juejin.cn/post/6997723668155482149)

[5 分钟实现用 docker 搭建 Redis 集群模式和哨兵模式](https://xie.infoq.cn/article/bcef50b14c4e787d65f92a2b9)
[docker-compose编排redis集群](https://segmentfault.com/a/1190000039024451)
[Docker 搭建 Redis Cluster 集群环境](https://xie.infoq.cn/article/a5536b928edd12beb32fcabf9)
[]()
[]()






