/**
 * 项目名称：quickstart-redis 
 * 文件名：RedisClusterClient.java
 * 版本信息：
 * 日期：2017年3月14日
 * Copyright yangzl Corporation 2017
 * 版权所有 *
 */
package org.quickstart.redis.jedis.cluster;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/**
 * RedisClusterClient
 * 
 * @author：youngzil@163.com
 * @2017年3月14日 下午4:56:41
 * @version 1.0
 */
public class RedisClusterClient {

    /**
     * 客户端连接超时时间
     */
    private final static int TIME_OUT = 3000;

    /**
     * soket超时时间
     */
    private final static int SO_TIME_OUT = 3000;

    /**
     * 最大尝试次数
     */
    private final static int MAX_ATTEMP = 5;

    private String serverInfo = "10.1.245.93:8001,10.1.245.93:8002,10.1.245.93:8007,10.1.245.93:8004,10.1.245.93:8005,10.1.245.93:8006";
    // private String serverInfo = "10.21.38.66:7000,10.21.38.66:7001,10.21.38.66:7002,10.21.38.66:7003,10.21.38.66:7004,10.21.38.66:7005";

    private Set<HostAndPort> getClusterInfo(String serverInfo) {
        Set<HostAndPort> set = new HashSet<HostAndPort>();
        if (serverInfo == null || "".equals(serverInfo)) {
            throw new RuntimeException("The serverInfo can not be empty");
        }
        String ipPort[] = serverInfo.split(",");
        int len = ipPort.length;
        for (int i = 0; i < len; i++) {
            String server[] = ipPort[i].split(":");
            set.add(new HostAndPort(server[0], Integer.parseInt(server[1])));
            // set.add(new HostAndPort("10.1.245.93", Integer.parseInt(server[1])));
        }
        return set;
    }

    @Test
    public void test() {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(GenericObjectPoolConfig.DEFAULT_MAX_TOTAL * 10);
        poolConfig.setMaxIdle(GenericObjectPoolConfig.DEFAULT_MAX_IDLE * 5);
        poolConfig.setMinIdle(GenericObjectPoolConfig.DEFAULT_MAX_IDLE * 2);
        // JedisPool.borrowObject最大等待时间
        poolConfig.setMaxWaitMillis(1000L);
        // 开启jmx
        poolConfig.setJmxEnabled(true);

        Set<HostAndPort> jedisClusterNodes = getClusterInfo(serverInfo);
        // Jedis Cluster will attempt to discover cluster nodes automatically
        // JedisCluster jc = new JedisCluster(jedisClusterNodes);
        // jc.auth("123456");//这种方式不支持
        JedisCluster jc = new JedisCluster(jedisClusterNodes, TIME_OUT, SO_TIME_OUT, MAX_ATTEMP, poolConfig);
        // JedisCluster jc2 = new JedisCluster(jedisClusterNodes, TIME_OUT, SO_TIME_OUT, MAX_ATTEMP, "123456", poolConfig);
        jc.set("username", "boonya");

        /*jc.setnx("username", "boonya");
        jc.set("username", "boonya", "NX", "EX", 1000L);*/

        String value = jc.get("username");
        System.out.println(value);
    }

}
