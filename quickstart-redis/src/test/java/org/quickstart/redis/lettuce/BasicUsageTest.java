package org.quickstart.redis.lettuce;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.TransactionResult;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.async.RedisStringAsyncCommands;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import io.lettuce.core.api.reactive.RedisStringReactiveCommands;
import io.lettuce.core.api.sync.RedisStringCommands;
import io.lettuce.core.pubsub.RedisPubSubListener;
import io.lettuce.core.pubsub.api.sync.RedisPubSubCommands;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import redis.clients.jedis.Transaction;

/**
 * @author youngzil@163.com
 * @description TODO
 * @createTime 2019-07-06 23:11
 */
public class BasicUsageTest {

  @Test
  public void testSync() {

    // client
    RedisClient client = RedisClient.create("redis://localhost");

    // 设定超时时间为20s
    // RedisClient client = RedisClient.create(RedisURI.create("localhost", 6379));
    // client.setDefaultTimeout(Duration.ofSeconds(20));

    // RedisURI redisUri = RedisURI.Builder.redis("localhost").withPassword("authentication").withDatabase(2).build();
    // RedisClient client = RedisClient.create(redisUri);

    // RedisClient redisClient = RedisClient.create("redis://password@localhost:6379/");
    // RedisURI redisUri = RedisURI.create("redis://authentication@localhost/2");
    // RedisClient client = RedisClient.create(redisUri);

    // connection, 线程安全的长连接，连接丢失时会自动重连，直到调用close关闭连接。
    StatefulRedisConnection<String, String> connection = client.connect();

    // sync, 默认超时时间为60s.
    RedisStringCommands<String, String> sync = connection.sync();
    sync.set("host", "note.abeffect.com");
    String value = sync.get("host");
    System.out.println(value);

    // close connection
    connection.close();

    // shutdown
    client.shutdown();

  }

  @Test
  public void testAsync() throws ExecutionException, InterruptedException {

    RedisClient client = RedisClient.create("redis://localhost");
    StatefulRedisConnection<String, String> connection = client.connect();
    RedisStringAsyncCommands<String, String> async = connection.async();
    RedisFuture<String> set = async.set("key", "value");
    RedisFuture<String> get = async.get("key");

    // async.awaitAll(set, get);

    String setStr = set.get();
    String getStr = get.get();

  }

  @Test
  public void testReactive() {

    // 异步操作提供Future<T>来获取data或object，通常会对接收到的data或object执行后续操作。Reactive API就是用来处理这些后续操作。Publisher<T>将pull模式转化为push模式，利用函数onNext(T)，onError(Exception)，onCompleted()。

    RedisClient client = RedisClient.create("redis://localhost");
    StatefulRedisConnection<String, String> connection = client.connect();
    RedisStringReactiveCommands<String, String> reactive = connection.reactive();
    Mono<String> set = reactive.set("key", "value");

    reactive.set("Ben", "Benvalue");
    reactive.set("Michael", "Michaelvalue");
    reactive.set("Mark", "Markvalue");

    Mono<String> get = reactive.get("key");

    set.subscribe();

    String getStr = get.block();

    reactive.get("key").subscribe(value -> System.out.println(value));

    System.out.println("==============================================");

    Flux.just("Ben", "Michael", "Mark")//
        .flatMap(reactive::get)//
        // .flatMap(value -> reactive.set("result", value))//
        .subscribe();

  }

  @Test
  public void testPubSub() {

    RedisClient client = RedisClient.create("redis://localhost");
    RedisPubSubCommands<String, String> connection = client.connectPubSub().sync();
    // connection.addListener(new RedisPubSubListener<String, String>() { ... });
    connection.subscribe("channel");

  }

  @Test
  public void testTransaction() throws ExecutionException, InterruptedException {

    RedisClient client = RedisClient.create("redis://localhost");

    // 异步command中transaction：
    RedisAsyncCommands<String, String> async = client.connect().async();
    RedisFuture<String> multi = async.multi();
    RedisFuture<String> set = async.set("key", "value");
    RedisFuture<TransactionResult> exec = async.exec();
    TransactionResult result = exec.get();
    String setResult = set.get();

    System.out.println("result=" + result);
    System.out.println("setResult=" + setResult);

    // Transaction with reactive API:
    RedisReactiveCommands<String, String> reactive = client.connect().reactive();
    reactive.multi().subscribe(multiResponse -> {
      reactive.set("key", "1").subscribe();
      reactive.incr("key").subscribe();
      reactive.exec().subscribe();
    });

  }

}
