package org.quickstart.caffeine.sample;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

/**
 * @author youngzil@163.com
 * @description TODO
 * @createTime 2019-07-06 09:43
 */
public class CacheRefreshTest {

  @Test
  public  void refresh(){
    Caffeine.newBuilder()//
        .refreshAfterWrite(1, TimeUnit.MINUTES)//
        .build(k -> DataObject.get("Data for " + k));
  }

}
