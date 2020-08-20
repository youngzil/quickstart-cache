Redis集群要求至少3个主节点3个从节点，共计6个节点。



安装依赖包
安装gcc套装
yum install -y cpp binutils glibc glibc-kernheaders glibc-common glibc-devel gcc make

升级gcc
yum -y install centos-release-scl
yum -y install devtoolset-9-gcc devtoolset-9-gcc-c++ devtoolset-9-binutils
scl enable devtoolset-9 bash

设置永久升级
echo "source /opt/rh/devtoolset-9/enable" >>/etc/profile




修改系统配置
关闭NetworkManager
[root@localhost ~]# systemctl disable NetworkManager --关闭自启动
[root@localhost ~]# systemctl stop NetworkManager
[root@localhost ~]# systemctl status NetworkManager

关闭防火墙
systemctl stop firewalld.service
systemctl disable firewalld.service --关闭自启动
firewall-cmd --state

vi /etc/selinux/config
SELINUX=disabled



参考
https://developer.aliyun.com/article/767317

