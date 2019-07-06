package org.quickstart.caffeine.sample;

import static org.junit.Assert.assertEquals;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.junit.Test;

/**
 * @author youngzil@163.com
 * @description TODO
 * @createTime 2019-07-06 09:48
 */
public class CacheStatsTest {

  @Test
  public void testStats() {

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
