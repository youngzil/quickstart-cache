package org.quickstart.redis.lettuce;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisStringAsyncCommands;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author youngzil@163.com
 * @description TODO
 * @createTime 2019-07-06 23:45
 */
public class AsynchronousAPI {
  public static void main(String[] args) {
    // client
    RedisClient client = RedisClient.create("redis://localhost");

    // connect
    StatefulRedisConnection<String, String> connection = client.connect();

    // async
    RedisStringAsyncCommands<String, String> async = connection.async();

    RedisFuture<String> future = async.get("host");

    try {
      String value = future.get(60, TimeUnit.SECONDS);
      System.out.println(value);
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      e.printStackTrace();
    }

    connection.close();
    client.shutdown();
  }
}
