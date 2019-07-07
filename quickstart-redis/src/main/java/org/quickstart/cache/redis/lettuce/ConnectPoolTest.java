package org.quickstart.cache.redis.lettuce;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.TransactionResult;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.support.AsyncConnectionPoolSupport;
import io.lettuce.core.support.AsyncPool;
import io.lettuce.core.support.BoundedPoolConfig;
import io.lettuce.core.support.ConnectionPoolSupport;
import java.util.concurrent.CompletableFuture;
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

}
