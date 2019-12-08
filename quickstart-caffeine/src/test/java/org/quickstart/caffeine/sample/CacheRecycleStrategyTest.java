package org.quickstart.caffeine.sample;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.benmanes.caffeine.cache.LoadingCache;

/**
 * @author youngzil@163.com
 * @description TODO
 * @createTime 2019-07-06 08:58
 */
public class CacheRecycleStrategyTest {

  // Caffeine 有三个值回收策略：基于大小，基于时间和基于引用。

  @Test
  public void baseSizeOrWeight() {

    LoadingCache<String, DataObject> cache = Caffeine.newBuilder()//
        .maximumSize(1)//
        .build(k -> DataObject.get("Data for " + k));

    assertEquals(0, cache.estimatedSize());

    cache.get("A");
    assertEquals(1, cache.estimatedSize());

    cache.get("B");
    // 在获取缓存大小之前，我们调用了 cleanUp 方法。这是因为缓存回收被异步执行，这种方式有助于等待回收工作完成。
    cache.cleanUp();
    assertEquals(1, cache.estimatedSize());

    LoadingCache<String, DataObject> cache2 = Caffeine.newBuilder()//
        .maximumWeight(10)//
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

    // 让我们使用 expireAfterAccess 方法配置访问后过期策略：
    LoadingCache<String, DataObject> cache = Caffeine.newBuilder()//
        .expireAfterAccess(5, TimeUnit.MINUTES)//
        .build(k -> DataObject.get("Data for " + k));

    // 要配置写入后到期策略，我们使用 expireAfterWrite 方法：
    cache = Caffeine.newBuilder()//
        .expireAfterWrite(10, TimeUnit.SECONDS)//
        .weakKeys()//
        .weakValues()//
        .build(k -> DataObject.get("Data for " + k));

    // 要初始化自定义策略，我们需要实现 Expiry 接口：
    cache = Caffeine.newBuilder()//
        .expireAfter(new Expiry<String, DataObject>() {
          @Override
          public long expireAfterCreate(String key, DataObject value, long currentTime) {
            return value.getData().length() * 1000;
          }

          @Override
          public long expireAfterUpdate(String key, DataObject value, long currentTime, long currentDuration) {
            return currentDuration;
          }

          @Override
          public long expireAfterRead(String key, DataObject value, long currentTime, long currentDuration) {
            return currentDuration;
          }
        })//
        .build(k -> DataObject.get("Data for " + k));

  }

  @Test
  public void baseRefence() {

    LoadingCache<String, DataObject> cache = Caffeine.newBuilder()//
        .expireAfterWrite(10, TimeUnit.SECONDS)//
        .weakKeys()//
        .weakValues()//
        .build(k -> DataObject.get("Data for " + k));

    cache = Caffeine.newBuilder()//
        .expireAfterWrite(10, TimeUnit.SECONDS)//
        .softValues()//
        .build(k -> DataObject.get("Data for " + k));

  }

}
