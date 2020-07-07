/**
 * 项目名称：quickstart-redis 
 * 文件名：RedisClusterTest.java
 * 版本信息：
 * 日期：2017年3月14日
 * Copyright yangzl Corporation 2017
 * 版权所有 *
 */
package org.quickstart.redis.jedis.cluster;

import java.util.HashSet;
import java.util.Set;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/**
 * RedisClusterTest
 * 
 * @author：youngzil@163.com
 * @2017年3月14日 下午5:28:21
 * @version 1.0
 */
public class RedisClusterTest {
    private static RedisClusterTest redisClusterComponent = new RedisClusterTest();

    private static final String HOST = "109.168.1.41";
    /**
     * redisCluster客户端
     */
    private JedisCluster redisCluster;
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

    private RedisClusterTest() {
        try {
            // redis节点信息
            Set<HostAndPort> nodeList = new HashSet<HostAndPort>();
            nodeList.add(new HostAndPort(HOST, 7000));
            nodeList.add(new HostAndPort(HOST, 7001));
            nodeList.add(new HostAndPort(HOST, 7002));
            nodeList.add(new HostAndPort(HOST, 7003));
            nodeList.add(new HostAndPort(HOST, 7004));
            nodeList.add(new HostAndPort(HOST, 7005));
            // redisCluster = new JedisCluster(nodeList, getCommonPoolConfig());
            // 不需要密码
            redisCluster = new JedisCluster(nodeList, TIME_OUT, SO_TIME_OUT, MAX_ATTEMP, getCommonPoolConfig());
            // 带有密码的
            // redisCluster = new JedisCluster(nodeList, TIME_OUT, SO_TIME_OUT, MAX_ATTEMP, "123456", getCommonPoolConfig());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * pool配置
     * 
     * @return
     */
    public static GenericObjectPoolConfig getCommonPoolConfig() {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(GenericObjectPoolConfig.DEFAULT_MAX_TOTAL * 10);
        poolConfig.setMaxIdle(GenericObjectPoolConfig.DEFAULT_MAX_IDLE * 5);
        poolConfig.setMinIdle(GenericObjectPoolConfig.DEFAULT_MAX_IDLE * 2);
        // JedisPool.borrowObject最大等待时间
        poolConfig.setMaxWaitMillis(1000L);
        // 开启jmx
        poolConfig.setJmxEnabled(true);
        return poolConfig;
    }

    public static RedisClusterTest getInstance() {
        return redisClusterComponent;
    }

    public void destroy() {
        if (redisCluster != null) {
          redisCluster.close();
        }
    }

    public JedisCluster getRedisCluster() {
        return redisCluster;
    }

    public static void main(String[] args) {
        JedisCluster cluster = RedisClusterTest.getInstance().getRedisCluster();
        cluster.set("foo", "testvalue");
        System.out.println(cluster.get("foo"));
    }
}
