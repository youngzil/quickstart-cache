package org.quickstart.jcache.example;

import java.util.HashMap;
import java.util.Map;
import javax.cache.integration.CacheLoader;
import javax.cache.integration.CacheLoaderException;

/**
 * @author youngzil@163.com
 * @description TODO
 * @createTime 2019/9/16 23:14
 */
public class SimpleCacheLoader implements CacheLoader<Integer, String> {

  public String load(Integer key) throws CacheLoaderException {
    return "fromCache" + key;
  }

  public Map<Integer, String> loadAll(Iterable<? extends Integer> keys) throws CacheLoaderException {
    Map<Integer, String> data = new HashMap<>();
    for (int key : keys) {
      data.put(key, load(key));
    }
    return data;
  }
}
