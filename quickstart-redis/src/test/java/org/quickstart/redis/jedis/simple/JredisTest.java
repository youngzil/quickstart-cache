/**
 * 项目名称：quickstart-redis 
 * 文件名：RedisJava.java
 * 版本信息：
 * 日期：2017年3月14日
 * Copyright yangzl Corporation 2017
 * 版权所有 *
 */
package org.quickstart.redis.jedis.simple;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

/**
 * RedisJava
 * 
 * @author：youngzil@163.com
 * @2017年3月14日 上午9:59:05
 * @version 1.0
 */
public class JredisTest {

    private Jedis jedis;

    private Jedis shardedJedis;

    @Before
    public void setup() {
        // 连接redis服务器，10.21.38.66
        jedis = new Jedis("10.21.38.66", 6379);
        // 权限认证
        jedis.auth("123456");

        // 连接redis服务器，10.21.38.66
        this.shardedJedis = new Jedis(new JedisShardInfo("10.21.38.66", 6379));
        // 权限认证
        shardedJedis.auth("123456");
    }

    /**
     * redis存储字符串
     */
    @Test
    public void testString() {
        // -----添加数据----------
        jedis.set("name", "xinxin");// 向key-->name中放入了value-->xinxin
        System.out.println(jedis.get("name"));// 执行结果：xinxin

        jedis.append("name", " is my lover"); // 拼接
        System.out.println(jedis.get("name"));

        jedis.del("name"); // 删除某个键
        System.out.println(jedis.get("name"));
        // 设置多个键值对
        jedis.mset("name", "liuling", "age", "23", "qq", "476777XXX");
        jedis.incr("age"); // 进行加1操作
        System.out.println(jedis.get("name") + "-" + jedis.get("age") + "-" + jedis.get("qq"));

    }

    /**
     * redis操作Map
     */
    @Test
    public void testMap() {
        // -----添加数据----------
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "xinxin");
        map.put("age", "22");
        map.put("qq", "123456");
        jedis.hmset("user", map);
        // 取出user中的name，执行结果:[minxr]-->注意结果是一个泛型的List
        // 第一个参数是存入redis中map对象的key，后面跟的是放入map中的对象的key，后面的key可以跟多个，是可变参数
        List<String> rsmap = jedis.hmget("user", "name", "age", "qq");
        System.out.println(rsmap);

        // 删除map中的某个键值
        jedis.hdel("user", "age");
        System.out.println(jedis.hmget("user", "age")); // 因为删除了，所以返回的是null
        System.out.println(jedis.hlen("user")); // 返回key为user的键中存放的值的个数2
        System.out.println(jedis.exists("user"));// 是否存在key为user的记录 返回true
        System.out.println(jedis.hkeys("user"));// 返回map对象中的所有key
        System.out.println(jedis.hvals("user"));// 返回map对象中的所有value

        Iterator<String> iter = jedis.hkeys("user").iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            System.out.println(key + ":" + jedis.hmget("user", key));
        }
    }

    /**
     * jedis操作List
     */
    @Test
    public void testList() {
        // 开始前，先移除所有的内容
        jedis.del("java framework");
        System.out.println(jedis.lrange("java framework", 0, -1));
        // 先向key java framework中存放三条数据
        jedis.lpush("java framework", "spring");
        jedis.lpush("java framework", "struts");
        jedis.lpush("java framework", "hibernate");
        // 再取出所有数据jedis.lrange是按范围取出，
        // 第一个是key，第二个是起始位置，第三个是结束位置，jedis.llen获取长度 -1表示取得所有
        System.out.println(jedis.lrange("java framework", 0, -1));
        // System.out.println(jedis.sort("java framework"));

        jedis.del("java framework");
        jedis.rpush("java framework", "spring");
        jedis.rpush("java framework", "struts");
        jedis.rpush("java framework", "hibernate");
        System.out.println(jedis.lrange("java framework", 0, -1));
    }

    /**
     * jedis操作Set
     */
    @Test
    public void testSet() {
        // jedis.flushDB();

        // 添加
        jedis.sadd("user", "liuling");
        jedis.sadd("user", "xinxin");
        jedis.sadd("user", "ling");
        jedis.sadd("user", "zhangxinxin");
        jedis.sadd("user", "who");
        // 移除noname
        jedis.srem("user", "who");
        System.out.println(jedis.smembers("user"));// 获取所有加入的value
        System.out.println(jedis.sismember("user", "who"));// 判断 who 是否是user集合的元素
        System.out.println(jedis.srandmember("user"));
        System.out.println(jedis.scard("user"));// 返回集合的元素个数
    }

    @Test
    public void test() throws InterruptedException {
        // jedis 排序
        // 注意，此处的rpush和lpush是List的操作。是一个双向链表（但从表现来看的）
        jedis.del("a");// 先清除数据，再加入数据进行测试
        jedis.rpush("a", "1");
        jedis.lpush("a", "6");
        jedis.lpush("a", "3");
        jedis.lpush("a", "9");
        System.out.println(jedis.lrange("a", 0, -1));// [9, 3, 6, 1]
        System.out.println(jedis.sort("a")); // [1, 3, 6, 9] //输入排序后结果
        System.out.println(jedis.lrange("a", 0, -1));
    }

    @Test
    public void testRedisPool() {
        RedisUtil.getJedis().set("newname", "中文测试");
        System.out.println(RedisUtil.getJedis().get("newname"));
    }


    @Test
    public void testKey() {
        System.out.println("=============key==========================");
        // 清空数据
        System.out.println(shardedJedis.flushDB());
        System.out.println(shardedJedis.echo("foo"));
        // 判断key否存在
        System.out.println(shardedJedis.exists("foo"));
        shardedJedis.set("key", "values");
        System.out.println(shardedJedis.exists("key"));
    }

    @Test
    public void testString2() {
        System.out.println("=============String==========================");
        // 清空数据
        System.out.println(shardedJedis.flushDB());
        // 存储数据
        shardedJedis.set("foo", "bar");
        System.out.println(shardedJedis.get("foo"));
        // 若key不存在，则存储
        shardedJedis.setnx("foo", "foo not exits");
        System.out.println(shardedJedis.get("foo"));
        // 覆盖数据
        shardedJedis.set("foo", "foo update");
        System.out.println(shardedJedis.get("foo"));
        // 追加数据
        shardedJedis.append("foo", " hello, world");
        System.out.println(shardedJedis.get("foo"));
        // 设置key的有效期，并存储数据
        shardedJedis.setex("foo", 2, "foo not exits");
        System.out.println(shardedJedis.get("foo"));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }
        System.out.println(shardedJedis.get("foo"));
        // 获取并更改数据
        shardedJedis.set("foo", "foo update");
        System.out.println(shardedJedis.getSet("foo", "foo modify"));
        // 截取value的值
        System.out.println(shardedJedis.getrange("foo", 1, 3));
        System.out.println(shardedJedis.mset("mset1", "mvalue1", "mset2", "mvalue2", "mset3", "mvalue3", "mset4", "mvalue4"));
        System.out.println(shardedJedis.mget("mset1", "mset2", "mset3", "mset4"));
        System.out.println(shardedJedis.del(new String[] {"foo", "foo1", "foo3"}));
    }

    @Test
    public void testList2() {
        System.out.println("=============list==========================");
        // 清空数据
        System.out.println(shardedJedis.flushDB());
        // 添加数据
        shardedJedis.lpush("lists", "vector");
        shardedJedis.lpush("lists", "ArrayList");
        shardedJedis.lpush("lists", "LinkedList");
        // 数组长度
        System.out.println(shardedJedis.llen("lists"));
        // 排序,数字没错
        // System.out.println(shardedJedis.sort("lists"));
        // 字串
        System.out.println(shardedJedis.lrange("lists", 0, 3));
        // 修改列表中单个值
        shardedJedis.lset("lists", 0, "hello list!");
        // 获取列表指定下标的值
        System.out.println(shardedJedis.lindex("lists", 1));
        // 删除列表指定下标的值
        System.out.println(shardedJedis.lrem("lists", 1, "vector"));
        // 删除区间以外的数据
        System.out.println(shardedJedis.ltrim("lists", 0, 1));
        // 列表出栈
        System.out.println(shardedJedis.lpop("lists"));
        // 整个列表值
        System.out.println(shardedJedis.lrange("lists", 0, -1));

    }

    @Test
    public void testSet2() {
        System.out.println("=============set==========================");
        // 清空数据
        System.out.println(shardedJedis.flushDB());
        // 添加数据
        shardedJedis.sadd("sets", "HashSet");
        shardedJedis.sadd("sets", "SortedSet");
        shardedJedis.sadd("sets", "TreeSet");
        // 判断value是否在列表中
        System.out.println(shardedJedis.sismember("sets", "TreeSet"));;
        // 整个列表值
        System.out.println(shardedJedis.smembers("sets"));
        // 删除指定元素
        System.out.println(shardedJedis.srem("sets", "SortedSet"));
        // 出栈
        System.out.println(shardedJedis.spop("sets"));
        System.out.println(shardedJedis.smembers("sets"));
        //
        shardedJedis.sadd("sets1", "HashSet1");
        shardedJedis.sadd("sets1", "SortedSet1");
        shardedJedis.sadd("sets1", "TreeSet");
        shardedJedis.sadd("sets2", "HashSet2");
        shardedJedis.sadd("sets2", "SortedSet1");
        shardedJedis.sadd("sets2", "TreeSet1");
        // 交集
        System.out.println(shardedJedis.sinter("sets1", "sets2"));
        // 并集
        System.out.println(shardedJedis.sunion("sets1", "sets2"));
        // 差集
        System.out.println(shardedJedis.sdiff("sets1", "sets2"));
    }

    @Test
    public void testSortedSet() {
        System.out.println("=============zset==========================");
        // 清空数据
        System.out.println(shardedJedis.flushDB());
        // 添加数据
        shardedJedis.zadd("zset", 10.1, "hello");
        shardedJedis.zadd("zset", 10.0, ":");
        shardedJedis.zadd("zset", 9.0, "zset");
        shardedJedis.zadd("zset", 11.0, "zset!");
        // 元素个数
        System.out.println(shardedJedis.zcard("zset"));
        // 元素下标
        System.out.println(shardedJedis.zscore("zset", "zset"));
        // 集合子集
        System.out.println(shardedJedis.zrange("zset", 0, -1));
        // 删除元素
        System.out.println(shardedJedis.zrem("zset", "zset!"));
        System.out.println(shardedJedis.zcount("zset", 9.5, 10.5));
        // 整个集合值
        System.out.println(shardedJedis.zrange("zset", 0, -1));
    }

    @Test
    public void testHash() {
        System.out.println("=============hash==========================");
        // 清空数据
        System.out.println(shardedJedis.flushDB());
        // 添加数据
        shardedJedis.hset("hashs", "entryKey", "entryValue");
        shardedJedis.hset("hashs", "entryKey1", "entryValue1");
        shardedJedis.hset("hashs", "entryKey2", "entryValue2");
        // 判断某个值是否存在
        System.out.println(shardedJedis.hexists("hashs", "entryKey"));
        // 获取指定的值
        System.out.println(shardedJedis.hget("hashs", "entryKey")); // 批量获取指定的值
        System.out.println(shardedJedis.hmget("hashs", "entryKey", "entryKey1"));
        // 删除指定的值
        System.out.println(shardedJedis.hdel("hashs", "entryKey"));
        // 为key中的域 field 的值加上增量 increment
        System.out.println(shardedJedis.hincrBy("hashs", "entryKey", 123l));
        // 获取所有的keys
        System.out.println(shardedJedis.hkeys("hashs"));
        // 获取所有的values
        System.out.println(shardedJedis.hvals("hashs"));
    }

    @Test
    public void testSlowLogs() {
        shardedJedis.slowlogGet();
    }

}
