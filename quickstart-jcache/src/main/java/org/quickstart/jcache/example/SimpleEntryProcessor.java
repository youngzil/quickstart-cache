package org.quickstart.jcache.example;

import static org.junit.Assert.assertEquals;
import java.io.Serializable;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.processor.EntryProcessor;
import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.MutableEntry;
import javax.cache.spi.CachingProvider;
import org.junit.Test;

/**
 * @author youngzil@163.com
 * @description TODO
 * @createTime 2019/9/16 23:08
 */
public class SimpleEntryProcessor implements EntryProcessor<String, String, String>, Serializable {

  /**
   *
   */
  private static final long serialVersionUID = -5616476363722945132L;

  public String process(MutableEntry<String, String> entry, Object... args) throws EntryProcessorException {

    if (entry.exists()) {
      String current = entry.getValue();
      entry.setValue(current + " - modified");
      return current;
    }
    return null;
  }

  @Test
  public void whenModifyValue_thenCorrect() {

    CachingProvider cachingProvider = Caching.getCachingProvider();
    CacheManager cacheManager = cachingProvider.getCacheManager();
    MutableConfiguration<String, String> config = new MutableConfiguration<>();
    Cache<String, String> cache = cacheManager.createCache("simpleCache", config);

    cache.invoke("key", new SimpleEntryProcessor());

    assertEquals("value - modified", cache.get("key"));
  }

}
