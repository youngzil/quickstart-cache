package org.quickstart.jetcache.example2;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;
import com.alicp.jetcache.anno.CachePenetrationProtect;
import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;

/**
 * @author youngzil@163.com
 * @description TODO
 * @createTime 2019/9/16 22:15
 */
public interface SummaryService {

  // CachePenetrationProtect annotation indicates to synchronize load operation in multi-thread environment.

  @Cached(expire = 3600, cacheType = CacheType.REMOTE)
  @CacheRefresh(refresh = 1800, stopRefreshAfterLastAccess = 3600, timeUnit = TimeUnit.SECONDS)
  @CachePenetrationProtect//多线程同步读
  BigDecimal summaryOfToday(long catagoryId);
}
