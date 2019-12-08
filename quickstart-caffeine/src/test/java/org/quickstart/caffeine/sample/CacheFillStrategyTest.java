package org.quickstart.caffeine.sample;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

/**
 * @author youngzil@163.com
 * @description TODO
 * @createTime 2019-07-06 08:27
 */
public class CacheFillStrategyTest {

  // 手动
  @Test
  public void manualFill() {

    Cache<String, DataObject> cache = Caffeine.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES).maximumSize(100).build();

    // 使用 getIfPresent 方法从缓存中获取值。如果缓存中不存指定的值，则方法将返回 null：
    String key = "A";
    DataObject dataObject = cache.getIfPresent(key);
    System.out.println("dataObject=" + dataObject);

    // 使用 put 方法手动填充缓存：
    // cache.put(key, dataObject);//NullPointerException，因为Value必须非空
    cache.put(key, DataObject.get("SSS"));
    dataObject = cache.getIfPresent(key);
    System.out.println("dataObject=" + dataObject);

    // 我们也可以使用 get 方法获取值，该方法将一个参数为 key 的 Function 作为参数传入。如果缓存中不存在该 key，则该函数将用于提供默认值，该值在计算后插入缓存中：
    // get 方法可以以原子方式执行计算。这意味着你只进行一次计算 —— 即使有多个线程同时请求该值。这就是为什么使用 get 要优于 getIfPresent
    dataObject = cache.get(key, k -> DataObject.get("Data for A"));
    assertNotNull(dataObject);
    assertEquals("Data for A", dataObject.getData());

    // 有时我们需要手动触发一些缓存的值失效：
    cache.invalidate(key);
    dataObject = cache.getIfPresent(key);
    assertNull(dataObject);

  }

  // 同步加载
  @Test
  public void syncFill() {

    LoadingCache<String, DataObject> cache = Caffeine.newBuilder()//
        .maximumSize(100)//
        .expireAfterWrite(1, TimeUnit.MINUTES)//
        .build(k -> DataObject.get("Data for " + k));

    String key = "A";
    DataObject dataObject = cache.get(key);
    assertNotNull(dataObject);
    assertEquals("Data for " + key, dataObject.getData());

    Map<String, DataObject> dataObjectMap = cache.getAll(Arrays.asList("A", "B", "C"));
    assertEquals(3, dataObjectMap.size());
    System.out.println(dataObjectMap);

  }

  // 异步加载
  @Test
  public void AsyncFill() throws ExecutionException, InterruptedException {

    AsyncLoadingCache<String, DataObject> cache = Caffeine.newBuilder()//
        .maximumSize(100)//
        .expireAfterWrite(1, TimeUnit.MINUTES)//
        .buildAsync(k -> DataObject.get("Data for " + k));

    // 我们可以以相同的方式使用 get 和 getAll 方法，同时考虑到他们返回的是 CompletableFuture：
    String key = "A";
    cache.get(key).thenAccept(dataObject -> {
      assertNotNull(dataObject);
      assertEquals("Data for " + key, dataObject.getData());
    });

    cache.getAll(Arrays.asList("A", "B", "C"))//
        .thenAccept(dataObjectMap -> assertEquals(3, dataObjectMap.size()));

    ExecutorService executorService = Executors.newCachedThreadPool();

    AsyncLoadingCache<String, String> cache2 = Caffeine.newBuilder()//
        .executor(executorService)//
        .maximumSize(100)//
        .expireAfterWrite(1, TimeUnit.MINUTES)//
        // 测试删除监听器
        // .removalListener((K k, V v,RemovalCause cause)->{
        // System.out.print("k:"+k+"....V:"+v+"....cause"+cause);
        // logger.error("k:"+k+"....V:"+v+"....cause"+cause);
        // })
        .buildAsync(k -> k + System.currentTimeMillis());

    AsyncLoadingCache<String, String> cache3 = Caffeine.newBuilder()//
        .executor(executorService)//
        .maximumSize(10_000)//
        .expireAfterWrite(10, TimeUnit.MINUTES)
        // Either: Build with a synchronous computation that is wrapped as asynchronous
        // .buildAsync(key -> createExpensiveGraph(key));
        // Or: Build with a asynchronous computation that returns a future
        .buildAsync((k, executor) -> CompletableFuture.supplyAsync(() -> {
          System.out.println("key=" + k);
          return key + System.currentTimeMillis();
        }, executor));

    cache3.get("a").whenComplete((r, t) -> System.out.println("r=" + r)).get();

  }

}
