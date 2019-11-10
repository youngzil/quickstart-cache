package org.quickstart.codis.example;

import org.junit.Test;

import io.codis.jodis.JedisResourcePool;
import io.codis.jodis.RoundRobinJedisPool;
import redis.clients.jedis.Jedis;

/**
 * @author youngzil@163.com
 * @description TODO
 * @createTime 2019/11/10 20:17
 */
public class CodisTest {

  @Test
  public void testCodisV3() {

    JedisResourcePool jedisPool = RoundRobinJedisPool.create().curatorClient("zkserver:2181", 30000).zkProxyDir("/jodis/xxx").build();
    try (Jedis jedis = jedisPool.getResource()) {
      jedis.set("foo", "bar");
      String value = jedis.get("foo");
      System.out.println(value);
    }

  }

  @Test
  public void testCodisV2() {

    JedisResourcePool jedisPool = RoundRobinJedisPool.create().curatorClient("zkserver:2181", 30000).zkProxyDir("/zk/codis/db_xxx/proxy").build();
    try (Jedis jedis = jedisPool.getResource()) {
      jedis.set("foo", "bar");
      String value = jedis.get("foo");
      System.out.println(value);
    }

  }

}
