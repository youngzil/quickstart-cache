package org.quickstart.redis.lettuce;

import io.lettuce.core.KeyScanCursor;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.ScanArgs;
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
import io.lettuce.core.support.BoundedAsyncPool;
import io.lettuce.core.support.BoundedPoolConfig;
import io.lettuce.core.support.ConnectionPoolSupport;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Test;

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
    list.add(RedisURI.create("redis://20.26.85.227:7000"));
    list.add(RedisURI.create("redis://20.26.85.227:7001"));
    list.add(RedisURI.create("redis://20.26.85.227:7002"));
    list.add(RedisURI.create("redis://20.26.85.227:7003"));
    list.add(RedisURI.create("redis://20.26.85.227:7004"));
    list.add(RedisURI.create("redis://20.26.85.227:7005"));
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
        .createBoundedObjectPool(() -> client.connectAsync(StringCodec.UTF8, RedisURI.create("20.26.85.227", 7000)), BoundedPoolConfig.create());

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

    // RedisClusterClient clusterClient = RedisClusterClient.create(RedisURI.create("20.26.85.227", 7000));

    ArrayList<RedisURI> list = new ArrayList<>();
    list.add(RedisURI.create("redis://20.26.85.227:7000"));
    list.add(RedisURI.create("redis://20.26.85.227:7001"));
    list.add(RedisURI.create("redis://20.26.85.227:7002"));
    list.add(RedisURI.create("redis://20.26.85.227:7003"));
    list.add(RedisURI.create("redis://20.26.85.227:7004"));
    list.add(RedisURI.create("redis://20.26.85.227:7005"));
    RedisClusterClient clusterClient = RedisClusterClient.create(list);

    AsyncPool<StatefulRedisClusterConnection<String, String>> pool =
        AsyncConnectionPoolSupport.createBoundedObjectPool(() -> clusterClient.connectAsync(StringCodec.UTF8), BoundedPoolConfig.create());

    // execute work
    CompletableFuture<String> setResult = pool.acquire().thenCompose(connection -> {

      RedisAdvancedClusterAsyncCommands<String, String> async = connection.async();

      // IntStream.range(0, 100).forEach(i -> {
      //   async.set("key" + i, "value" + i);
      // });

      return async.set("key22222", "value22222").whenComplete(
          (s, throwable) -> {
            pool.release(connection);
            // terminating
            pool.closeAsync();

            // after pool completion
            clusterClient.shutdownAsync();
          });

    });

    CompletableFuture.allOf(setResult);

    System.out.println("dddd");

  }

  @Test
  public void testAsyncClusterUsage2() throws Exception {

    List<RedisURI> list = new ArrayList<>();
    list.add(RedisURI.create("redis://10.1.243.23:7000"));
    list.add(RedisURI.create("redis://10.1.243.23:7001"));
    list.add(RedisURI.create("redis://10.1.243.23:7002"));
    list.add(RedisURI.create("redis://10.1.243.23:7003"));
    list.add(RedisURI.create("redis://10.1.243.23:7004"));
    list.add(RedisURI.create("redis://10.1.243.23:7005"));

    RedisClusterClient redisClusterClient = RedisClusterClient.create(list);
    redisClusterClient.getPartitions();

    AsyncPool<StatefulRedisClusterConnection<String, String>> pool = AsyncConnectionPoolSupport.createBoundedObjectPool(
        () -> redisClusterClient.connectAsync(StringCodec.UTF8), BoundedPoolConfig.builder().maxIdle(10).maxTotal(20).minIdle(1).build());

    pool.acquire().thenCompose(connection -> {
      RedisAsyncCommands<String, String> asynComannd = (RedisAsyncCommands<String, String>) ((StatefulRedisClusterConnection<String, String>) connection)
          .async();
      connection.setAutoFlushCommands(false);
      asynComannd.del("sysCode1:configName1:paramName1");
      // asynComannd.flushCommands();
      connection.flushCommands();
      pool.release(connection);

      return null;
    });

    BoundedAsyncPool boundedAsyncPool = (BoundedAsyncPool) pool;
    System.out.println(boundedAsyncPool.getMaxTotal());


  }

  @Test
  public void operCluster() {
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
    commands.set("hello", "hello world");
    String str = commands.get("hello");
    System.out.println(str);

    /**
     * 异步执行命令
     */
    RedisAdvancedClusterAsyncCommands<String, String> asyncCommands = connect.async();
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


  @Test
  public void scanAndKeys() {
    List<RedisURI> list = new ArrayList<>();
    list.add(RedisURI.create("redis://10.1.243.23:7000"));
    // list.add(RedisURI.create("redis://10.1.243.23:28001"));
    // list.add(RedisURI.create("redis://10.1.243.23:28002"));
    // list.add(RedisURI.create("redis://10.1.243.23:28003"));
    // list.add(RedisURI.create("redis://10.1.243.23:28004"));
    // list.add(RedisURI.create("redis://10.1.243.23:28005"));
    // list.add(RedisURI.create("redis://10.1.243.23:28006"));

    RedisClusterClient client = RedisClusterClient.create(list);

    StatefulRedisClusterConnection<String, String> connect = client.connect();

    /**
     * 同步执行命令
     */
    RedisAdvancedClusterCommands<String, String> commands = connect.sync();

    long startTime = System.currentTimeMillis();

    List<String> keys = commands.keys("ServiceGroup:*");
    System.out.println("keys count = " + keys.size());

    long midTime = System.currentTimeMillis();


    /*Set<String> result = new HashSet<>();
    ScanArgs args = new ScanArgs();
    args.match("ServiceGroup*");
    args.limit(200);
    KeyScanCursor<String> currentCursor = null;
    do {
      if (currentCursor == null) {
        currentCursor = commands.scan(args);
      } else {
        currentCursor = commands.scan(currentCursor, args);
      }
      result.addAll(currentCursor.getKeys());
    }
    while (!currentCursor.isFinished());
    System.out.println(result);*/

    // ScanIterator<String> scan = ScanIterator.scan(commands, ScanArgs.Builder.limit(50).match("ServiceGroup*"));

    ScanArgs scanArgs = ScanArgs.Builder.limit(50).match("ServiceGroup*");
    KeyScanCursor<String> cursor = commands.scan(scanArgs);
    Set<String> scans = new HashSet<>();
    while (!cursor.isFinished()) {
      scans.addAll(cursor.getKeys());
      System.out.println(scans.size());
      cursor = commands.scan(cursor, scanArgs);
    }
    System.out.println("scans count = " + scans.size());

    long endTime = System.currentTimeMillis();

    System.out.println("keys time=" + (midTime - startTime) + ",scans time = " + (endTime - midTime));

    connect.close();
    client.shutdown();
  }

}
