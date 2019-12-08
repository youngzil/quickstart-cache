package org.quickstart.jcache.example;

import java.io.Serializable;
import javax.cache.event.CacheEntryCreatedListener;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryListenerException;
import javax.cache.event.CacheEntryUpdatedListener;

/**
 * @author youngzil@163.com
 * @description TODO
 * @createTime 2019/9/16 23:12
 */
public class SimpleCacheEntryListener implements CacheEntryCreatedListener<String, String>, CacheEntryUpdatedListener<String, String>, Serializable {

  /**
   *
   */
  private static final long serialVersionUID = -712657810462878763L;
  private boolean updated;
  private boolean created;

  public boolean getUpdated() {
    return this.updated;
  }

  public boolean getCreated() {
    return this.created;
  }

  public void onUpdated(Iterable<CacheEntryEvent<? extends String, ? extends String>> events) throws CacheEntryListenerException {
    this.updated = true;
  }

  public void onCreated(Iterable<CacheEntryEvent<? extends String, ? extends String>> events) throws CacheEntryListenerException {
    this.created = true;
  }

  /*  @Test
  public void whenRunEvent_thenCorrect() throws InterruptedException {
    this.listenerConfiguration
        = new MutableCacheEntryListenerConfiguration<String, String>(
        FactoryBuilder.factoryOf(this.listener), null, false, true);
    this.cache.registerCacheEntryListener(this.listenerConfiguration);
  
    assertEquals(false, this.listener.getCreated());
  
    this.cache.put("key", "value");
  
    assertEquals(true, this.listener.getCreated());
    assertEquals(false, this.listener.getUpdated());
  
    this.cache.put("key", "newValue");
  
    assertEquals(true, this.listener.getUpdated());
  }*/

}
