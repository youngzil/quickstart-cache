package org.quickstart.guava.cache.sample;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.cache.Weigher;
import com.google.common.collect.MapMaker;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

/**
 * @author youngzil@163.com
 * @description TODO
 * @createTime 2019-07-10 23:47
 */
public class GuavaCacheTest {

  @Test
  public void testMap() throws InterruptedException {
    // MapMaker 用于构建一个可以自动将key或者value包装成WeakRefrence的ConcurrentMap，并且效率和ConcurrentMap相当
    ConcurrentMap<String, Integer> map = new MapMaker().weakKeys().makeMap();
    String key = "key1";
    map.put(key, 1);
    key = null;
    System.gc();
    Thread.sleep(1000);
    System.out.println("key->" + map.get(key));
  }

  @Test
  public void testCacheBuilder() throws ExecutionException {

    // 可以通过CacheBuilder类构建一个缓存对象，CacheBuilder类采用builder设计模式，它的每个方法都返回CacheBuilder本身，直到build方法被调用。

    // CacheBuilder 用于构建一个缓存，改缓存具有自动加载、缓存回收等功能
    Cache<String, String> cache = CacheBuilder.newBuilder().concurrencyLevel(20)// 设置并发等级，即最高并发
        .maximumWeight(3)// 设置最大权重（这里基于权重的缓存回收，比如配套Wigher）
        .maximumSize(1000)// 设置最大存储
        .expireAfterWrite(3, TimeUnit.SECONDS)// s设置过期时间
        // 可以通过weakKeys和weakValues方法指定Cache只保存对缓存记录key和value的弱引用。这样当没有其他强引用指向key和value时，key和value对象就会被垃圾回收器回收。
        .weakValues().recordStats() // 开启统计信息开关
        .removalListener(new RemovalListener<String, String>() {// 元素被回收的监听器
          @Override
          public void onRemoval(RemovalNotification<String, String> notification) {
            System.out.println(notification.getKey() + "被移除,原因:" + notification.getCause());
          }
        }).weigher(new Weigher<String, String>() {// 设置Wigher，用于计算每个元素的权重
          @Override
          public int weigh(String key, String value) {
            return 1;
          }
        }).build();

    cache.put("1", "1");
    cache.put("2", "2");
    cache.put("3", "3");
    cache.getIfPresent("1");
    cache.put("4", "4");
    System.out.println(cache.asMap());
    cache.invalidate("4");
    System.out.println(cache.asMap());

    // Guava可以保证当有多个线程同时访问Cache中的一个key时，如果key对应的记录不存在，Guava只会启动一个线程执行get方法中Callable参数对应的任务加载数据存到缓存。当加载完数据后，任何线程中的get方法都会获取到key对应的值。
    String value = cache.get("key", new Callable<String>() {
      public String call() throws Exception {
        System.out.println("load1"); // 加载数据线程执行标志
        Thread.sleep(1000); // 模拟加载时间
        return "auto load by Callable";
      }
    });
    System.out.println("thread1 " + value);

    cache.put("key1", "value1");
    cache.put("key2", "value2");
    cache.put("key3", "value3");
    cache.put("key4", "value4");

    cache.getIfPresent("key1");
    cache.getIfPresent("key2");
    cache.getIfPresent("key3");
    cache.getIfPresent("key4");
    cache.getIfPresent("key5");
    cache.getIfPresent("key6");

    System.out.println(cache.stats()); // 获取统计信息

  }

  @Test
  public void testLoadingCache() throws ExecutionException {

    // LoadingCache是Cache的子接口，相比较于Cache，当从LoadingCache中读取一个指定key的记录时，如果该记录不存在，则LoadingCache可以自动执行加载数据到缓存的操作。

    CacheLoader<String, String> loader = new CacheLoader<String, String>() {
      public String load(String key) throws Exception {
        Thread.sleep(1000); // 休眠1s，模拟加载数据
        System.out.println(key + " is loaded from a cacheLoader!");
        return key + "'s value";
      }
    };

    LoadingCache<String, String> loadingCache = CacheBuilder.newBuilder().maximumSize(3).build(loader);// 在构建时指定自动加载器

    loadingCache.get("key1");
    loadingCache.get("key2");
    loadingCache.get("key3");

  }


  @Test
  public void whenCacheMiss_thenValueIsComputed() {
    CacheLoader<String, String> loader;
    loader = new CacheLoader<String, String>() {
      @Override
      public String load(String key) {
        return key.toUpperCase();
      }
    };

    LoadingCache<String, String> cache;
    cache = CacheBuilder.newBuilder().build(loader);

    System.out.println(cache.size());
    System.out.println(cache.getUnchecked("hello"));
    System.out.println(cache.size());

  }


  @Test
  public void whenCacheReachMaxSize_thenEviction() {
    CacheLoader<String, String> loader;
    loader = new CacheLoader<String, String>() {
      @Override
      public String load(String key) {
        return key.toUpperCase();
      }
    };
    LoadingCache<String, String> cache;
    cache = CacheBuilder.newBuilder().maximumSize(3).build(loader);

    cache.getUnchecked("first");
    cache.getUnchecked("second");
    cache.getUnchecked("third");
    cache.getUnchecked("forth");
    assertEquals(3, cache.size());
    assertNull(cache.getIfPresent("first"));
    assertEquals("FORTH", cache.getIfPresent("forth"));
  }


}
