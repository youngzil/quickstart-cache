/**
 * 项目名称：quickstart-redis 
 * 文件名：ClusterTest.java
 * 版本信息：
 * 日期：2017年3月14日
 * Copyright yangzl Corporation 2017
 * 版权所有 *
 */
package org.quickstart.redis.jedis.cluster;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import junit.framework.TestCase;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/**
 * ClusterTest 
 *  
 * @author：youngzil@163.com
 * @2017年3月14日 下午5:15:31 
 * @version 1.0
 */
/**
 * 
 * @packge com.boonya.redis.RedisClusterTest
 * @date 2015年10月21日 下午2:31:38
 * @author pengjunlin
 * @comment jedis客户端的坑. 1)cluster环境下redis的slave不接受任何读写操作，
 * 
 *          2)client端不支持keys批量操作,不支持select dbNum操作，只有一个db:select 0
 * 
 *          3)JedisCluster 的info()等单机函数无法调用,返回(No way to dispatch this command to Redis Cluster)错误，.
 * @update
 * @from http://blog.csdn.net/myrainblues/article/details/25881535
 */
public class ClusterTest {

    private static String host = "192.168.1.41";

    private static JedisCluster cluster;

    static {
        // 只给集群里一个实例就可以
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        jedisClusterNodes.add(new HostAndPort(host, 7000));
        jedisClusterNodes.add(new HostAndPort(host, 7001));
        jedisClusterNodes.add(new HostAndPort(host, 7002));
        jedisClusterNodes.add(new HostAndPort(host, 7003));
        jedisClusterNodes.add(new HostAndPort(host, 7004));
        jedisClusterNodes.add(new HostAndPort(host, 7005));

        cluster = new JedisCluster(jedisClusterNodes);
        // cluster.auth("123456");
    }

    @Test
    public void testSetString() {
        // cluster.set("username", "boonya");
        System.out.println("set username " + cluster.get("username"));
    }

    @Test
    public void testAppendString() {
        cluster.append("username", " peng");
        System.out.println("append username " + cluster.get("username"));
    }

    @Test
    public void testDelString() {
        cluster.set("username", "boonya xnn");
        System.out.println("del-get username " + cluster.get("username"));
        cluster.del("username");
        System.out.println("del-did username " + cluster.get("username"));
    }

    @Test
    public void testMap() {
        // -----添加数据----------
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "boonya");
        map.put("age", "88");
        map.put("qq", "123456");
        cluster.hmset("user", map);
        // 第一个参数是存入redis中map对象的key，后面跟的是放入map中的对象的key，后面的key可以跟多个，是可变参数
        List<String> rsmap = cluster.hmget("user", "name", "age", "qq");
        System.out.println(rsmap);

        // 删除map中的某个键值
        cluster.hdel("user", "age");
        System.out.println(cluster.hmget("user", "age")); // 因为删除了，所以返回的是null
        System.out.println(cluster.hlen("user")); // 返回key为user的键中存放的值的个数2
        System.out.println(cluster.exists("user"));// 是否存在key为user的记录 返回true
        System.out.println(cluster.hkeys("user"));// 返回map对象中的所有key
        System.out.println(cluster.hvals("user"));// 返回map对象中的所有value

        Iterator<String> iter = cluster.hkeys("user").iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            System.out.println(key + ":" + cluster.hmget("user", key));
        }
    }

    @Test
    public void testList() {
        // 开始前，先移除所有的内容
        cluster.del("java framework");
        System.out.println(cluster.lrange("java framework", 0, -1));
        // 先向key java framework中存放三条数据
        cluster.lpush("java framework", "spring");
        cluster.lpush("java framework", "struts");
        cluster.lpush("java framework", "hibernate");
        // 再取出所有数据jedis.lrange是按范围取出，
        // 第一个是key，第二个是起始位置，第三个是结束位置，jedis.llen获取长度 -1表示取得所有
        System.out.println(cluster.lrange("java framework", 0, -1));

        cluster.del("java framework");
        cluster.rpush("java framework", "spring");
        cluster.rpush("java framework", "struts");
        cluster.rpush("java framework", "hibernate");
        System.out.println(cluster.lrange("java framework", 0, -1));
    }

    @Test
    public void testSet() {
        // 添加
        cluster.sadd("user", "boonya");
        cluster.sadd("user", "niuniu");
        cluster.sadd("user", "ling");
        cluster.sadd("user", "guoniuniu");
        cluster.sadd("user", "who");
        // cluster.sadd("user",new String []{
        // "boonya","niuniu","ling","guoniuniu","who"});

        // 移除noname
        cluster.srem("user", "who");
        System.out.println(cluster.smembers("user"));// 获取所有加入的value
        System.out.println(cluster.sismember("user", "who"));// 判断 who
                                                             // 是否是user集合的元素
        System.out.println(cluster.srandmember("user"));
        System.out.println(cluster.scard("user"));// 返回集合的元素个数
    }

    @Test
    public void testUserSession() {
        // -----添加数据----------
        Map<String, String> map = new HashMap<String, String>();
        map.put("zhangsan", "zhangsan-v");
        map.put("lisi", "lisi-v");
        map.put("wangwu", "wangwu-v");
        cluster.hmset("usersession", map);
        // 第一个参数是存入redis中map对象的key，后面跟的是放入map中的对象的key，后面的key可以跟多个，是可变参数
        List<String> rsmap = cluster.hmget("usersession", "zhangsan", "lisi");
        System.out.println(rsmap);
    }
}
