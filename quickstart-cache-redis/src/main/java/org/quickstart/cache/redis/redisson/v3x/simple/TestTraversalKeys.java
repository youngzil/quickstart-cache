/**
 * 项目名称：quickstart-redis 
 * 文件名：TestTraversalKeys.java
 * 版本信息：
 * 日期：2017年11月20日
 * Copyright asiainfo Corporation 2017
 * 版权所有 *
 */
package org.quickstart.cache.redis.redisson.v3x.simple;

import java.util.Iterator;

import org.redisson.Redisson;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * TestTraversalKeys
 * 
 * @author：yangzl@asiainfo.com
 * @2017年11月20日 下午4:21:28
 * @since 1.0
 */
public class TestTraversalKeys {

    public static void main(String[] args) {
        Config config = new Config();

        config.useSingleServer()//
                .setAddress("redis://10.11.20.103:6379")//
                .setPassword("aabbcc");//

        RedissonClient client = Redisson.create(config);

        // 设置10个值
        for (int i = 0; i < 100; i++) {
            RAtomicLong rl = client.getAtomicLong("test" + i);
            rl.set(i);
        }

        // 循环keys
        RKeys keys = client.getKeys();
        Iterable<String> keysIterable = keys.getKeys();
        Iterator<String> keysIterator = keysIterable.iterator();
        while (keysIterator.hasNext()) {
            String key = keysIterator.next();
            System.out.println("keyName=" + key);
        }

        client.shutdown();

    }

}
