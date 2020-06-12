package org.quickstart.caffeine.sample;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * @author youngzil@163.com
 * @description TODO
 * @createTime 2019-07-06 08:58
 */
public class CacheRecycleStrategyTest {

  // Caffeine 有三个值回收策略：基于大小，基于时间和基于引用。

  @Test
  public void baseSizeOrWeight() {

    //maximumWeight与maximumSize不可以同时使用。

    // 根据缓存的计数进行驱逐
    LoadingCache<String, DataObject> cache = Caffeine.newBuilder()//
        .maximumSize(10000)//
        .build(k -> DataObject.get("Data for " + k));

    assertEquals(0, cache.estimatedSize());

    cache.get("A");
    assertEquals(1, cache.estimatedSize());

    cache.get("B");
    // 在获取缓存大小之前，我们调用了 cleanUp 方法。这是因为缓存回收被异步执行，这种方式有助于等待回收工作完成。
    cache.cleanUp();
    assertEquals(1, cache.estimatedSize());

    // 根据缓存的权重来进行驱逐（权重只是用于确定缓存大小，不会用于决定该缓存是否被驱逐）
    LoadingCache<String, DataObject> cache2 = Caffeine.newBuilder()//
        .maximumWeight(10000)//
        .weigher((k, v) -> 5)//
        .build(k -> DataObject.get("Data for " + k));

    assertEquals(0, cache2.estimatedSize());

    cache2.get("A");
    assertEquals(1, cache2.estimatedSize());

    cache2.get("B");
    assertEquals(2, cache2.estimatedSize());

    cache.get("C");
    cache.cleanUp();
    assertEquals(2, cache.estimatedSize());

  }

  @Test
  public void baseDate() {

    // 基于固定的到期策略进行退出

    // 让我们使用 expireAfterAccess 方法配置访问后过期策略：
    LoadingCache<String, DataObject> cache = Caffeine.newBuilder()//
        .expireAfterAccess(5, TimeUnit.MINUTES)//
        .build(k -> DataObject.get("Data for " + k));

    // 要配置写入后到期策略，我们使用 expireAfterWrite 方法：
    LoadingCache<String, DataObject> cache2 = Caffeine.newBuilder()//
        .expireAfterWrite(10, TimeUnit.SECONDS)//
        .weakKeys()//
        .weakValues()//
        .build(k -> DataObject.get("Data for " + k));

    // 基于不同的到期策略进行退出
    // 要初始化自定义策略，我们需要实现 Expiry 接口：
    LoadingCache<String, DataObject> cache3 = Caffeine.newBuilder()//
        .expireAfter(new Expiry<String, DataObject>() {
          @Override
          public long expireAfterCreate(String key, DataObject value, long currentTime) {
            return value.getData().length() * 1000;
            // return TimeUnit.SECONDS.toNanos(seconds);
          }

          @Override
          public long expireAfterUpdate(@NonNull String key,@NonNull DataObject value, long currentTime, long currentDuration) {
            return currentDuration;
          }

          @Override
          public long expireAfterRead(@NonNull String key,@NonNull DataObject value, long currentTime,@NonNegative long currentDuration) {
            return currentDuration;
          }
        })//
        .build(k -> DataObject.get("Data for " + k));

  }

  @Test
  public void baseRefence() {

    // 当key和value都没有引用时驱逐缓存
    LoadingCache<String, DataObject> cache = Caffeine.newBuilder()//
        .expireAfterWrite(10, TimeUnit.SECONDS)//
        .weakKeys()//
        .weakValues()//
        .build(k -> DataObject.get("Data for " + k));

    // 当垃圾收集器需要释放内存时驱逐
    cache = Caffeine.newBuilder()//
        .expireAfterWrite(10, TimeUnit.SECONDS)//
        .softValues()//
        .build(k -> DataObject.get("Data for " + k));

    // 注意：AsyncLoadingCache不支持弱引用和软引用。
    // Caffeine.weakValues()和Caffeine.softValues()不可以一起使用。

  }

}
