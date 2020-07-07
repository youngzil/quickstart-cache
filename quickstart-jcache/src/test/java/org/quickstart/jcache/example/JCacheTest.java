package org.quickstart.jcache.example;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;

/**
 * @author youngzil@163.com
 * @description TODO
 * @createTime 2019/9/16 23:06
 */
public class JCacheTest {

  public static void main(String[] args) {

    CachingProvider cachingProvider = Caching.getCachingProvider();
    CacheManager cacheManager = cachingProvider.getCacheManager();
    MutableConfiguration<String, String> config = new MutableConfiguration<>();
    Cache<String, String> cache = cacheManager.createCache("simpleCache", config);
    cache.put("key1", "value1");
    cache.put("key2", "value2");

    System.out.println(cache.get("key1"));
    System.out.println(cache.get("key2"));

    cacheManager.close();
  }


}
