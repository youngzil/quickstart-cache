package org.quickstart.jcache.example;

import static org.junit.Assert.assertEquals;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.FactoryBuilder;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;
import org.junit.Before;
import org.junit.Test;

/**
 * @author youngzil@163.com
 * @description TODO
 * @createTime 2019/9/16 23:14
 */
public class CacheLoaderTest {

  private Cache<Integer, String> cache;

  @Before
  public void setup() {
    CachingProvider cachingProvider = Caching.getCachingProvider();
    CacheManager cacheManager = cachingProvider.getCacheManager();
    MutableConfiguration<Integer, String> config
        = new MutableConfiguration<>()
        .setReadThrough(true)
        .setCacheLoaderFactory(new FactoryBuilder.SingletonFactory(
            new SimpleCacheLoader()));
    this.cache = cacheManager.createCache("SimpleCache", config);
  }

  @Test
  public void whenReadingFromStorage_thenCorrect() {
    for (int i = 1; i < 4; i++) {
      String value = cache.get(i);

      assertEquals("fromCache" + i, value);
    }
  }
}
