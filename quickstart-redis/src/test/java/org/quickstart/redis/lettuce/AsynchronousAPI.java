package org.quickstart.redis.lettuce;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisHashAsyncCommands;
import io.lettuce.core.api.async.RedisStringAsyncCommands;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.Test;
import sun.jvm.hotspot.HelloWorld;

/**
 * @author youngzil@163.com
 * @description TODO
 * @createTime 2019-07-06 23:45
 */
public class AsynchronousAPI {

  @Test
  public void testHashAsync() throws ExecutionException, InterruptedException {

    // client
    // Syntax: redis://[password@]host[:port][/databaseNumber]
    RedisClient client = RedisClient.create("redis://localhost");

    // connect
    StatefulRedisConnection<String, String> connection = client.connect();

    // async
    RedisHashAsyncCommands<String, String> hashAsyncCommands = connection.async();

    Map<String,String> map = new HashMap<>();
    map.put("test","test");
    map.put("test1","test1");
    map.put("test2","test2");
    map.put("test3","test3");
    RedisFuture<String> future = hashAsyncCommands.hmset("map",map);
    RedisFuture<Map<String,String>> ff = hashAsyncCommands.hgetall("map");
    System.out.println(ff.get());

    Map<String,String> map2 = new HashMap<>();
    map2.put("test1","test11");
    map2.put("test3","test33");

    hashAsyncCommands.hmset("map",map2);
    RedisFuture<Map<String,String>> ff2 = hashAsyncCommands.hgetall("map");
    System.out.println(ff2.get());

    hashAsyncCommands.hdel("map","test2");
    RedisFuture<Map<String,String>> ff3 = hashAsyncCommands.hgetall("map");
    System.out.println(ff3.get());

    try {
      String value = future.get(60, TimeUnit.SECONDS);
      System.out.println(value);
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      e.printStackTrace();
    }

    connection.close();
    client.shutdown();

  }

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
