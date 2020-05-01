package org.quickstart.redis.lettuce;

import io.lettuce.core.RedisFuture;
import io.lettuce.core.support.BoundedAsyncPool;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import java.util.concurrent.ExecutionException;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Test;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.TransactionResult;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.support.AsyncConnectionPoolSupport;
import io.lettuce.core.support.AsyncPool;
import io.lettuce.core.support.BoundedPoolConfig;
import io.lettuce.core.support.ConnectionPoolSupport;

/**
 * @author youngzil@163.com
 * @description TODO
 * @createTime 2019-07-07 10:29
 */
public class ConnectPoolTest {

  @Test
  public void testBaseUsage() throws Exception {
    RedisClient client = RedisClient.create(RedisURI.create("localhost", 6379));

    GenericObjectPool<StatefulRedisConnection<String, String>> pool =
        ConnectionPoolSupport.createGenericObjectPool(() -> client.connect(), new GenericObjectPoolConfig());

    // executing work
    try (StatefulRedisConnection<String, String> connection = pool.borrowObject()) {

      RedisCommands<String, String> commands = connection.sync();
      commands.multi();
      commands.set("key", "value");
      commands.set("key2", "value2");
      commands.exec();

      System.out.println(commands.get("key"));
      System.out.println(commands.get("key2"));

    }

    // terminating
    pool.close();
    client.shutdown();
  }

  @Test
  public void testClusterUsageZero() throws Exception {

    ArrayList<RedisURI> list = new ArrayList<>();
    list.add(RedisURI.create("redis://10.1.243.23:7000"));
    list.add(RedisURI.create("redis://10.1.243.23:7001"));
    list.add(RedisURI.create("redis://10.1.243.23:7002"));
    list.add(RedisURI.create("redis://10.1.243.23:7003"));
    list.add(RedisURI.create("redis://10.1.243.23:7004"));
    list.add(RedisURI.create("redis://10.1.243.23:7005"));
    RedisClusterClient client = RedisClusterClient.create(list);
    // RedisClusterClient client = RedisClusterClient.create("redis://192.168.37.128:7000");
    StatefulRedisClusterConnection<String, String> connect = client.connect();

    /* 同步执行的命令 */
    RedisAdvancedClusterCommands<String, String> commands = connect.sync();

    commands.set("test", "hello");

    String str = commands.get("test");
    System.out.println(str);

    /* 异步执行的命令 */
    // RedisAdvancedClusterAsyncCommands<String, String> commands= connect.async();
    // RedisFuture<String> future = commands.get("test2");
    // try {
    // String str = future.get();
    // System.out.println(str);
    // } catch (InterruptedException e) {
    // e.printStackTrace();
    // } catch (ExecutionException e) {
    // e.printStackTrace();
    // }

    connect.close();
    client.shutdown();

  }

  @Test
  public void testClusterUsage() throws Exception {

    RedisClusterClient clusterClient = RedisClusterClient.create(RedisURI.create("localhost", 6379));

    GenericObjectPool<StatefulRedisClusterConnection<String, String>> pool =
        ConnectionPoolSupport.createGenericObjectPool(() -> clusterClient.connect(), new GenericObjectPoolConfig());

    // execute work
    try (StatefulRedisClusterConnection<String, String> connection = pool.borrowObject()) {
      connection.sync().set("key", "value");
      connection.sync().blpop(10, "list");
    }

    // terminating
    pool.close();
    clusterClient.shutdown();

  }

  @Test
  public void testAsyncBaseUsage() throws Exception {

    RedisClient client = RedisClient.create();

    AsyncPool<StatefulRedisConnection<String, String>> pool = AsyncConnectionPoolSupport
        .createBoundedObjectPool(() -> client.connectAsync(StringCodec.UTF8, RedisURI.create("localhost", 6379)), BoundedPoolConfig.create());

    // execute work
    CompletableFuture<TransactionResult> transactionResult = pool.acquire().thenCompose(connection -> {

      RedisAsyncCommands<String, String> async = connection.async();

      async.multi();
      async.set("key", "value");
      async.set("key2", "value2");
      return async.exec().whenComplete((s, throwable) -> pool.release(connection));
    });

    // terminating
    pool.closeAsync();

    // after pool completion
    client.shutdownAsync();

  }

  @Test
  public void testAsyncClusterUsage() throws Exception {

    RedisClusterClient clusterClient = RedisClusterClient.create(RedisURI.create("localhost", 6379));

    AsyncPool<StatefulRedisClusterConnection<String, String>> pool =
        AsyncConnectionPoolSupport.createBoundedObjectPool(() -> clusterClient.connectAsync(StringCodec.UTF8), BoundedPoolConfig.create());

    // execute work
    CompletableFuture<String> setResult = pool.acquire().thenCompose(connection -> {

      RedisAdvancedClusterAsyncCommands<String, String> async = connection.async();

      async.set("key", "value");
      return async.set("key2", "value2").whenComplete((s, throwable) -> pool.release(connection));

    });

    // terminating
    pool.closeAsync();

    // after pool completion
    clusterClient.shutdownAsync();

  }

  @Test
  public void testAsyncClusterUsage2() throws Exception {

    List<RedisURI> list = new ArrayList<>();
    list.add(RedisURI.create("redis://20.26.37.179:28001"));
    list.add(RedisURI.create("redis://20.26.37.179:28002"));
    list.add(RedisURI.create("redis://20.26.37.180:28003"));
    list.add(RedisURI.create("redis://20.26.37.180:28004"));
    list.add(RedisURI.create("redis://20.26.37.181:28005"));
    list.add(RedisURI.create("redis://20.26.37.181:28006"));
    RedisClusterClient redisClusterClient = RedisClusterClient.create(list);


    AsyncPool<StatefulRedisClusterConnection<String, String>> pool = AsyncConnectionPoolSupport.createBoundedObjectPool(
        () -> redisClusterClient.connectAsync(StringCodec.UTF8), BoundedPoolConfig.builder().maxIdle(10).maxTotal(20).minIdle(1).build());

    pool.acquire();

    BoundedAsyncPool boundedAsyncPool = (BoundedAsyncPool) pool;
    System.out.println(boundedAsyncPool.getMaxTotal());


  }

  @Test
  public  void operCluster(){
    List<RedisURI> list = new ArrayList<>();
    list.add(RedisURI.create("redis://20.26.37.179:28001"));
    list.add(RedisURI.create("redis://20.26.37.179:28002"));
    list.add(RedisURI.create("redis://20.26.37.180:28003"));
    list.add(RedisURI.create("redis://20.26.37.180:28004"));
    list.add(RedisURI.create("redis://20.26.37.181:28005"));
    list.add(RedisURI.create("redis://20.26.37.181:28006"));
    RedisClusterClient client = RedisClusterClient.create(list);

    StatefulRedisClusterConnection<String, String> connect = client.connect();

    /**
     * 同步执行命令
     */
    RedisAdvancedClusterCommands<String, String> commands = connect.sync();
    commands.set("hello","hello world");
    String str = commands.get("hello");
    System.out.println(str);

    /**
     * 异步执行命令
     */
    RedisAdvancedClusterAsyncCommands<String,String> asyncCommands = connect.async();
    RedisFuture<String> future = asyncCommands.get("hello");

    try {
      String str1 = future.get();
      System.out.println(str1);
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }

    connect.close();
    client.shutdown();
  }

}
