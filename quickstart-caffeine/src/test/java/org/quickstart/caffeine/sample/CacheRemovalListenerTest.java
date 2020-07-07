package org.quickstart.caffeine.sample;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

/**
 * @author yangzl
 * @description TODO
 * @createTime 2020/6/12 16:17
 */
public class CacheRemovalListenerTest {

  @Test
  public void testRemovalListener() throws InterruptedException {

    // 3. 移除事件监听#

    Cache<String, Object> cache = Caffeine.newBuilder()
        .maximumSize(2)
        .removalListener((String key, Object value, RemovalCause cause) ->
            System.out.printf("Key %s was removed,value=%s (%s)%n", key,value, cause))
        .build();

    int i = 0;
    while (true) {
      cache.put("test" + i, i);
      i++;
      TimeUnit.SECONDS.sleep(3);
    }

  }

}
