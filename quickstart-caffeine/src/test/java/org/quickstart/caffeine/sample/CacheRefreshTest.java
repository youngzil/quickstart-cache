package org.quickstart.caffeine.sample;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * @author youngzil@163.com
 * @description TODO
 * @createTime 2019-07-06 09:43
 */
public class CacheRefreshTest {

  @Test
  public void refresh() {
    Caffeine.newBuilder()//
        .refreshAfterWrite(1, TimeUnit.MINUTES)//
        .build(k -> DataObject.get("Data for " + k));
  }

}
