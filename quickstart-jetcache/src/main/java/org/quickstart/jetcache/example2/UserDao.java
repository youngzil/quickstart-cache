package org.quickstart.jetcache.example2;

import java.util.concurrent.TimeUnit;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.alicp.jetcache.redis.RedisCacheBuilder;
import com.alicp.jetcache.support.FastjsonKeyConvertor;
import com.alicp.jetcache.support.JavaValueDecoder;
import com.alicp.jetcache.support.JavaValueEncoder;
import redis.clients.jedis.JedisPool;

/**
 * @author youngzil@163.com
 * @description TODO
 * @createTime 2019/9/16 22:18
 */
public class UserDao {

  // 创建一个两级缓存：本地和远程，本地缓存上限50，LRU淘汰规则
  @CreateCache(expire = 100, cacheType = CacheType.BOTH, localLimit = 50)
  private static Cache<Long, UserDO> userCache;

  public static void main(String[] args) {

    UserDO user = userCache.get(12345L);
    userCache.put(12345L, loadUserFromDataBase(12345L));
    userCache.remove(12345L);

    userCache.computeIfAbsent(1234567L, (key) -> loadUserFromDataBase(1234567L));

    GenericObjectPoolConfig pc = new GenericObjectPoolConfig();
    pc.setMinIdle(2);
    pc.setMaxIdle(10);
    pc.setMaxTotal(10);
    JedisPool pool = new JedisPool(pc, "localhost", 6379);
    Cache<Long, UserDO> userCache = RedisCacheBuilder.createRedisCacheBuilder()//
        .keyConvertor(FastjsonKeyConvertor.INSTANCE)//
        .valueEncoder(JavaValueEncoder.INSTANCE)//
        .valueDecoder(JavaValueDecoder.INSTANCE)//
        .jedisPool(pool)//
        .keyPrefix("userCache-")//
        .expireAfterWrite(200, TimeUnit.SECONDS)//
        .buildCache();

  }

  private static UserDO loadUserFromDataBase(long l) {
    return new UserDO();
  }

}
