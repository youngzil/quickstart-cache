package org.quickstart.jetcache.example2;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheGetResult;
import com.alicp.jetcache.ResultData;
import com.alicp.jetcache.anno.CachePenetrationProtect;
import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CreateCache;
import com.alicp.jetcache.embedded.CaffeineCacheBuilder;
import com.alicp.jetcache.support.FastjsonKeyConvertor;

/**
 * @author youngzil@163.com
 * @description TODO
 * @createTime 2019/9/16 22:22
 */
public class AdvancedExample {

  Cache<Object, Object> cache = CaffeineCacheBuilder.createCaffeineCacheBuilder()//
      .limit(100)//
      .expireAfterWrite(200, TimeUnit.SECONDS)//
      .keyConvertor(FastjsonKeyConvertor.INSTANCE)//
      .buildCache();

  public void testAsynchronous() {
    CacheGetResult r = cache.GET("userId");
    CompletionStage<ResultData> future = r.future();
    future.thenRun(() -> {
      if (r.isSuccess()) {
        System.out.println(r.getValue());
      }
    });
  }

  public void testDistributedLock() {

    cache.tryLockAndRun("key", 60, TimeUnit.SECONDS, () -> heavyDatabaseOperation());

  }

  public void heavyDatabaseOperation() {}



  @CreateCache
  @CacheRefresh(timeUnit = TimeUnit.MINUTES, refresh = 60, stopRefreshAfterLastAccess = 100)//自动刷新
  @CachePenetrationProtect //多线程同步读
  private Cache<String, Long> orderSumCache;

  @PostConstruct
  public void init() {
    orderSumCache.config().setLoader(this::loadOrderSumFromDatabase);
  }
  private Long loadOrderSumFromDatabase(String s) {
    return 10L;
  }

}
