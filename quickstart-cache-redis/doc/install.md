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
 
 
 mac 下安装也可以使用 homebrew，homebrew 是 mac 的包管理器。
1、执行 brew install redis
2、启动 redis，可以使用后台服务启动 brew services start redis。或者直接启动：redis-server /usr/local/etc/redis.conf
 
 
 