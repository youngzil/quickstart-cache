package org.quickstart.redis.lettuce;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.async.RedisStringAsyncCommands;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.async.RedisClusterAsyncCommands;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.support.AsyncConnectionPoolSupport;
import io.lettuce.core.support.AsyncPool;
import io.lettuce.core.support.BoundedPoolConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.Test;

/**
 * @author youngzil@163.com
 * @description TODO
 * @createTime 2019-07-06 23:31
 */
public class AsyncTest {

  @Test
  public void testAsync() throws ExecutionException, InterruptedException, TimeoutException {

    // 连接
    // Syntax: redis://[password@]host[:port][/databaseNumber]
    // RedisClient client = RedisClient.create("redis://localhost");
    String uri = "redis://127.0.0.1:6379";
    RedisClient client = RedisClient.create(uri);
    RedisAsyncCommands<String, String> commands = client.connect().async();

    AsyncPool<StatefulRedisConnection<String, String>> pool = AsyncConnectionPoolSupport.createBoundedObjectPool(
        () -> client.connectAsync(StringCodec.ASCII, RedisURI.create(uri)), BoundedPoolConfig.create());

    CompletableFuture<String> pingResponse = pool.acquire().thenCompose(c -> c.async().ping().whenComplete((s, throwable) -> {
      System.out.println("result=" + s);
      pool.release(c);
    }));

    // 使用阻塞的方式读取
    RedisFuture<String> future = commands.get("key");
    String value = future.get();
    System.out.println("value=" + value);

    // 设置阻塞读取时的超时时间
    RedisFuture<String> future2 = commands.get("key");
    String value2 = future2.get(1, TimeUnit.MINUTES);
    System.out.println("value2=" + value2);

    // 异步方式，当 RedisFuture<T>是完成状态时自动触发后面的动作
    RedisFuture<String> future3 = commands.get("key");
    future3.thenAccept(new Consumer<String>() {
      @Override
      public void accept(String s) {
        System.out.println("s=" + s);
      }
    });

    future3.thenAccept(System.out::println);

    // 使用独立的线程池
    // 为了防止阻塞默认的线程池，可以在单独的线程池中执行异步请求。

    Executor sharedExecutor = Executors.newFixedThreadPool(1);
    RedisFuture<String> future4 = commands.get("host");
    future4.thenAcceptAsync(System.out::println, sharedExecutor);

  }

  @Test
  public void testAsyncClusterUsage() throws Exception {

    // RedisClusterClient clusterClient = RedisClusterClient.create(RedisURI.create("20.26.85.227", 7000));

    List<String> hostList = new ArrayList<>();
    hostList.add("20.26.37.179:28001");
    hostList.add("20.26.37.179:28002");
    hostList.add("20.26.37.180:28003");
    hostList.add("20.26.37.180:28004");
    hostList.add("20.26.37.181:28005");
    hostList.add("20.26.37.181:28006");

    ArrayList<RedisURI> list = new ArrayList<>();
    hostList.forEach(host -> {
      RedisURI redisURI = RedisURI.create("redis://" + host);
      redisURI.setPassword("cmVkaXM=");
      list.add(redisURI);
    });

    RedisClusterClient clusterClient = RedisClusterClient.create(list);

    // RedisClusterClient redisClient = RedisClusterClient.create("redis://password@localhost:7379");

    // execute work
    // RedisAdvancedClusterAsyncCommands<String, String> clusterConnection = clusterClient.connect().async();

    AsyncPool<StatefulRedisClusterConnection<String, String>> pool = AsyncConnectionPoolSupport.createBoundedObjectPool(
        () -> clusterClient.connectAsync(StringCodec.UTF8), BoundedPoolConfig.create());

    // 必须有这一行
    clusterClient.getPartitions();

    // 没有上面一行，此行报否则报java.util.concurrent.ExecutionException: java.lang.IllegalStateException: Cannot allocate object
    // 下面的不会执行，异常没有出来
    // Object object = pool.acquire().get();
    // System.out.println(object);

    CompletableFuture<String> pingResponse = pool.acquire().thenCompose(c ->
        c.async().ping().whenComplete((s, throwable) -> {
          System.out.println("result=" + s);
          pool.release(c);

          // terminating
          // pool.closeAsync();

          // after pool completion
          // clusterClient.shutdownAsync();

        })
    );

    CompletableFuture<String> setResult = pool.acquire().thenCompose(connection -> {

      // RedisAdvancedClusterAsyncCommands<String, String> asyncCommands = connection.async();
      RedisClusterAsyncCommands asyncCommands = connection.async();

      /*IntStream.range(0, 100).forEach(i -> {
        asyncCommands.set("key" + i, "value" + i);
      });*/

      Stream.iterate(0, x -> x + 1).forEach(i -> {
        // asyncCommands.set("key" + i, "value" + i);
        RedisFuture<String>  resutl = asyncCommands.get("key24");
        try {
          System.out.println("成功get:" + resutl.get());
        } catch (InterruptedException e) {
          e.printStackTrace();
        } catch (ExecutionException e) {
          e.printStackTrace();
        }
        try {
          TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      });

      // IntStream.range(0, 100).forEach(i -> {
      //   asyncCommands.del("key" + i);
      // });

      asyncCommands.set("keyggggg", "value22222");
      asyncCommands.set("pluginsTemplate", "value22222");

      return asyncCommands.set("key22222", "value22222").whenComplete((s, throwable) -> {
        pool.release(connection);

        System.out.println("get key22222,result=" + s);

        // terminating
        pool.closeAsync();

        // after pool completion
        clusterClient.shutdownAsync();

      });

    });

    System.out.println("dddd");

    System.in.read();

  }

}
