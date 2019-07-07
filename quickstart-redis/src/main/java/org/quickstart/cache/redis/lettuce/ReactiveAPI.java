package org.quickstart.cache.redis.lettuce;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.reactive.RedisStringReactiveCommands;

/**
 * @author youngzil@163.com
 * @description TODO
 * @createTime 2019-07-06 23:49
 */
public class ReactiveAPI {
  public static void main(String[] args) {
    // client
    RedisClient client = RedisClient.create("redis://localhost");

    // connect
    StatefulRedisConnection<String, String> connection = client.connect();

    // reactive
    RedisStringReactiveCommands<String, String> reactive = connection.reactive();

    reactive.get("host").subscribe(System.out::println);

    try {
      Thread.sleep(1000L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    connection.close();
    client.shutdown();
  }
}
