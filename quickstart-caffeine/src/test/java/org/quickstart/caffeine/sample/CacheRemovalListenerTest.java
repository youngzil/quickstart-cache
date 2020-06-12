package org.quickstart.caffeine.sample;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import org.junit.Test;

/**
 * @author yangzl
 * @description TODO
 * @createTime 2020/6/12 16:17
 */
public class CacheRemovalListenerTest {

  @Test
  public  void testRemovalListener(){

    // 3. 移除事件监听#

    Cache<String, Object> cache = Caffeine.newBuilder()
        .removalListener((String key, Object value, RemovalCause cause) ->
            System.out.printf("Key %s was removed (%s)%n", key, cause))
        .build();
  }

}
