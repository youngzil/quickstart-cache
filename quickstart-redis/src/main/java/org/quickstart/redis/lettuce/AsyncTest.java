package org.quickstart.redis.lettuce;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisStringCommands;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
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
    RedisClient client = RedisClient.create("redis://localhost");
    RedisAsyncCommands<String, String> commands = client.connect().async();

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

}
