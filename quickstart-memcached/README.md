增删改查命令：add/set、replace、get、delete
遍历memcached：stats
Memcached 所有命令都是原子性的

---------------------------------------------------------------------------------------------------------------------


1、遍历memcache：
参考：
http://blog.csdn.net/aug0st/article/details/44702429
如何遍历memcache stats命令 memcache的stats命令包括：
1. stats
2. stats reset
3. stats malloc
4. stats maps
5. stats sizes
6. stats slabs
7. stats items
8. stats cachedump slab_id limit_num
9. stats detail [on|off|dump]
通过命令完成遍历 通过这些stats命令我们就可以完成memcache存储的内容的遍历，OK，下面我们通过telnet直接连接到memcache通过这些命令来完成相关的操作。


memcached 查看所有key：

//通过telnet链接进入memcache命令行管理界面
telnet 127.0.0.1 11211
//列举出所有keys
stats items
//通过stats items 的返回信息中的items id 获取key
//例如 itemid 为 7 第二个参数为列出的长度 0 为列出当前id对应的 key 名
stats cachedump 7 0
//通过如上获取 key 的值
get key //上一步得到的key名




1、启动Memcache 常用参数：http://blog.csdn.net/zzulp/article/details/7823511
-p <num>      设置TCP端口号(默认不设置为: 11211)
-U <num>      UDP监听端口(默认: 11211, 0 时关闭) 
-l <ip_addr>  绑定地址(默认:所有都允许,无论内外网或者本机更换IP，有安全隐患，若设置为127.0.0.1就只能本机访问)
-d                    以daemon方式运行
-u <username> 绑定使用指定用于运行进程<username>
-m <num>      允许最大内存用量，单位M (默认: 64 MB)
-P <file>     将PID写入文件<file>，这样可以使得后边进行快速进程终止, 需要与-d 一起使用

在linux下：./usr/local/bin/memcached -d -u root  -l 192.168.1.197 -m 2048 -p 12121
在window下：d:\App_Serv\memcached\memcached.exe -d RunService -l 127.0.0.1 -p 11211 -m 500
在windows下注册为服务后运行：
sc.exe create Memcached_srv binpath= “d:\App_Serv\memcached\memcached.exe -d RunService -p 11211 -m 500″start= auto
net start Memcached
 
2、连接
telnet 127.0.0.1 11211
3、基本命令 
您将使用五种基本 memcached 命令执行最简单的操作。这些命令和操作包括：
set
add
replace
get
delete
 
前三个命令是用于操作存储在 memcached 中的键值对的标准修改命令。它们都非常简单易用，且都使用如下 所示的语法：
command <key> <flags> <expiration time> <bytes>
<value>
最后两个基本命令是 get 和 delete。这些命令相当容易理解，并且使用了类似的语法，如下所示：
command <key>

表 1 定义了 memcached 修改命令的参数和用法。

表 1. memcached 修改命令参数
参数	用法
key	key 用于查找缓存值
flags	可以包括键值对的整型参数，客户机使用它存储关于键值对的额外信息
expiration time	在缓存中保存键值对的时间长度（以秒为单位，0 表示永远）
bytes	在缓存中存储的字节点
value	存储的值（始终位于第二行）
现在，我们来看看这些命令的实际使用。

使用实例：
set userId 0 0 5
add userId 0 0 5
replace accountId 0 0 5
get userId
delete bob
gets userId


set 命令用于向缓存添加新的键值对。如果键已经存在，则之前的值将被替换。
add 仅当缓存中不存在键时，add 命令才会向缓存中添加一个键值对。如果缓存中已经存在键，则之前的值将仍然保持相同，并且您将获得响应 NOT_STORED。
replace 仅当键已经存在时，replace 命令才会替换缓存中的键。如果缓存中不存在键，那么您将从 memcached 服务器接受到一条 NOT_STORED 响应。
get 命令用于检索与之前添加的键值对相关的值。如果这个键存在于缓存中，则返回相应的值。如果不存在，则不返回任何内容。
delete 如果该键存在于缓存中，则删除该值。如果不存在，则返回一条NOT_FOUND 消息。
gets 返回的信息稍微多一些：64 位的整型值非常像名称/值对的 “版本” 标识符。


---------------------------------------------------------------------------------------------------------------------


---------------------------------------------------------------------------------------------------------------------

