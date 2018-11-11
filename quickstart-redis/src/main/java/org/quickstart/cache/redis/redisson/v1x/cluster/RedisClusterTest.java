/**
 * 项目名称：quickstart-redis 
 * 文件名：RedisClusterTest.java
 * 版本信息：
 * 日期：2017年3月15日
 * Copyright asiainfo Corporation 2017
 * 版权所有 *
 */
package org.quickstart.cache.redis.redisson.v1x.cluster;

import org.junit.Before;
import org.junit.Test;
import org.redisson.Config;
import org.redisson.Redisson;
import org.redisson.core.RAtomicLong;

/**
 * RedisClusterTest
 * 
 * @author：youngzil@163.com
 * @2017年3月15日 上午11:18:59
 * @version 1.0
 */
public class RedisClusterTest {

    Redisson redisson;

    /**
     * 每次在测试方法运行之前 运行此方法 创建客户端连接服务器的redisson对象
     */
    @Before
    public void before() {
        String ip = "192.168.1.42";
        String port = "6379";
        String password = "123456";

        Config config = new Config();
        config.useClusterServers().setPassword(password).setScanInterval(2000) // cluster
                                                                               // state
                                                                               // scan
                                                                               // interval
                                                                               // in
                                                                               // milliseconds
                .addNodeAddress(ip + ":7000", ip + ":7001").addNodeAddress(ip + ":7002", ip + ":7003").addNodeAddress(ip + ":7004").addNodeAddress(ip + ":7005");
        redisson = Redisson.create(config);
        System.out.println("成功连接Redis Server cluster" + "\t");
    }

    @Test
    public void testString() {

    }

}
