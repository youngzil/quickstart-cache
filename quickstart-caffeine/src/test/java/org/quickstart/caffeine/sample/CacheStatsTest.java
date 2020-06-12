package org.quickstart.caffeine.sample;

import static org.junit.Assert.assertEquals;

import com.github.benmanes.caffeine.cache.Cache;
import org.junit.Test;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

/**
 * @author youngzil@163.com
 * @description TODO
 * @createTime 2019-07-06 09:48
 */
public class CacheStatsTest {

  @Test
  public void testStats() {

    // 与Guava Cache的统计一样。
    // 通过使用Caffeine.recordStats(), 可以转化成一个统计的集合. 通过 Cache.stats() 返回一个CacheStats。
    // CacheStats提供以下统计方法：
    // hitRate(): 返回缓存命中率
    // evictionCount(): 缓存回收数量
    // averageLoadPenalty(): 加载新值的平均时间

    LoadingCache<String, DataObject> cache = Caffeine.newBuilder()//
        .maximumSize(100)//
        .recordStats()//
        .build(k -> DataObject.get("Data for " + k));
    cache.get("A");
    cache.get("A");

    assertEquals(1, cache.stats().hitCount());
    assertEquals(1, cache.stats().missCount());

  }

}
