package org.quickstart.redis.jedis.simple;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class JedisSentinelTest {

    @Test
    public void testSentinel() {
        // 使用jedis来连接操作哨兵模式下的redis。连接redis不是直接连接的master的ip，而是来连接的哨兵们，把哨兵ip放在set集合中作为参数来创建连接池，内部会获取到当前master。
        // 在实践过程中对配置文件中的bind有了新的认识，我这里是把bind设为0.0.0.0，表示任何机器都可以连接redis服务器。

        String masterName = "mymaster";
        String password = "";
        // 设置参数
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(10);
        jedisPoolConfig.setMaxIdle(5);
        jedisPoolConfig.setMinIdle(5);
        // 哨兵信息，注意填写哨兵的地址
        Set<String> sentinels = new HashSet<String>();
        sentinels.add("118.190.156.123:26379");
        sentinels.add("118.190.156.123:26380");
        sentinels.add("118.190.156.123:26381");
        // 创建连接池
        JedisSentinelPool pool = new JedisSentinelPool(masterName, sentinels, jedisPoolConfig);
        //JedisSentinelPool pool = new JedisSentinelPool(masterName, sentinels,jedisPoolConfig, password);
        // 获取客户端
        Jedis jedis = pool.getResource();
        // 执行两个命令
        jedis.set("name", "sentinel-name");
        String value = jedis.get("name");
        System.out.println(value);
    }

    @Test
    public void testSentinel2() throws InterruptedException {

        Set<String> hosts = new HashSet<>();
        hosts.add("127.0.0.1:26379");
        //hosts.add("127.0.0.1:36379"); 配置多个哨兵

        JedisSentinelPool pool = new JedisSentinelPool("mymaster", hosts);
        Jedis jedis = null;

        for (int i = 0; i < 20; i++) {
            Thread.sleep(2000);

            try {

                jedis = pool.getResource();
                String v = randomString();
                jedis.set("hello", v);

                System.out.println(v + "-->" + jedis.get("hello").equals(v));

            } catch (Exception e) {
                System.out.println(" [ exception happened]" + e);
            }
        }
    }

    String randomString() {
        return UUID.randomUUID() + "";
    }

    @Test
    public void testRedisCluster() {

        Set<HostAndPort> jedisClusterNodes = new HashSet<>();
        //Jedis Cluster will attempt to discover cluster nodes automatically
        jedisClusterNodes.add(new HostAndPort("127.0.0.1", 6371));
        jedisClusterNodes.add(new HostAndPort("127.0.0.1", 6372));
        jedisClusterNodes.add(new HostAndPort("127.0.0.1", 6373));
        jedisClusterNodes.add(new HostAndPort("127.0.0.1", 6374));
        jedisClusterNodes.add(new HostAndPort("127.0.0.1", 6375));
        jedisClusterNodes.add(new HostAndPort("127.0.0.1", 6376));
        JedisCluster jc = new JedisCluster(jedisClusterNodes);
        jc.set("foo", "bar");
        String value = jc.get("foo");
        System.out.println(" ===> " + value);
    }

}
