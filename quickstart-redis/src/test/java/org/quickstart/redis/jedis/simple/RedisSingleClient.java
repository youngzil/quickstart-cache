/**
 * 项目名称：quickstart-redis 文件名：RedisSingleClient.java 版本信息： 日期：2017年3月14日 Copyright yangzl Corporation 2017 版权所有 *
 */
package org.quickstart.redis.jedis.simple;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.util.Pool;

/**
 * RedisSingleClient
 *
 * @version 1.0
 * @author：youngzil@163.com
 * @2017年3月14日 下午4:54:38
 */
public class RedisSingleClient {

  // 未分片客户端连接
  private Jedis jedis;
  // 未分片客户端连接池
  private JedisPool jedisPool;
  // 分片客户端连接
  private ShardedJedis shardedJedis;
  // 分片客户端连接池
  private ShardedJedisPool shardedJedisPool;
  // jedispool配置
  private JedisPoolConfig config;

  public void initJedisPoolConfig() {
    config = new JedisPoolConfig();
    config.setMaxTotal(100);
    config.setMaxIdle(10);
    config.setMaxWaitMillis(1000L);
    config.setTestOnBorrow(true);
    config.setTestOnReturn(true);
  }

  public void initJedisPool() {
    initJedisPoolConfig();
    jedisPool = new JedisPool(config, "192.168.11.176", 7379);
  }

  public void initShardedJedisPool() {
    initJedisPoolConfig();
    List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
    shards.add(new JedisShardInfo("192.168.11.176", 7379));
    shards.add(new JedisShardInfo("192.168.11.177", 7379));
    shards.add(new JedisShardInfo("192.168.11.178", 7379));
    // shardedJedisPool = new ShardedJedisPool(config, shards);
  }

  public Jedis getJedis() {
    jedis = this.jedisPool.getResource();
    return jedis;
  }

  public ShardedJedis getShardedJedis() {
    shardedJedis = this.shardedJedisPool.getResource();
    return shardedJedis;
  }

  public void returnResource(Pool pool, Jedis redis) {
    if (redis != null) {
      // pool.returnResourceObject(redis);
      redis.close();
    }
  }

  public void returnResource(Pool pool, ShardedJedis shardedJedis) {
    if (shardedJedis != null) {
      // pool.returnResourceObject(redis);
      shardedJedis.close();
    }
  }

  public void set(String key, String value) {
    try {
      jedis = getJedis();
      shardedJedis = getShardedJedis();
      jedis.set(key, value);
      shardedJedis.getShard("chiwei").set("chiwei", "lining");
    } catch (Exception e) {
      e.printStackTrace();
      // jedisPool.returnBrokenResource(jedis);
      // shardedJedisPool.returnBrokenResource(shardedJedis);
      jedis.close();
      shardedJedis.close();
    } finally {
      returnResource(jedisPool, jedis);
      returnResource(shardedJedisPool, shardedJedis);
    }
  }

  public String get(String key) {
    String value = null;
    try {
      jedis = getJedis();
      value = jedis.get(key);
    } catch (Exception e) {
      e.printStackTrace();
      // 释放资源
      // jedisPool.returnBrokenResource(jedis);
      jedis.close();
    } finally {
      returnResource(jedisPool, jedis);
    }
    return value;
  }

  public String getShardValue(String key) {
    String value = null;
    try {
      shardedJedis = getShardedJedis();
      value = shardedJedis.getShard(key).get(key);
    } catch (Exception e) {
      e.printStackTrace();
      // 释放资源
      // shardedJedisPool.returnBrokenResource(shardedJedis);
      shardedJedis.close();
    } finally {
      returnResource(shardedJedisPool, shardedJedis);
    }
    return value;
  }

  @Test
  public void test() {
    initJedisPool();
    initShardedJedisPool();
    jedis = getJedis();
    shardedJedis = getShardedJedis();
    jedis.set("momo", "nono");
    // shardedJedis.set("chiwei", "lining");
    System.out.println(jedis.get("momo"));
    // System.out.println(shardedJedis.get("chiwei"));
    shardedJedis.getShard("chiwei").set("chiwei", "lining");
    System.out.println(shardedJedis.getShard("chiwei").get("chiwei"));
  }

}
