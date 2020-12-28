package org.quickstart.redis.redisson;

import java.util.concurrent.TimeUnit;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * <p>描述: [功能描述] </p >
 *
 * @author yangzl
 * @version v1.0
 * @date 2020/12/6 09:40
 */
public class RedisRedlock {

  public static void main(String[] args) {
    Config config = new Config();
    config.useSentinelServers().addSentinelAddress("127.0.0.1:6369", "127.0.0.1:6379", "127.0.0.1:6389")
        .setMasterName("masterName")
        .setPassword("password").setDatabase(0);
    RedissonClient redissonClient = Redisson.create(config);
    // 还可以getFairLock(), getReadWriteLock()
    RLock redLock = redissonClient.getLock("REDLOCK_KEY");
    boolean isLock;
    try {
      isLock = redLock.tryLock();
      // 500ms拿不到锁, 就认为获取锁失败。10000ms即10s是锁失效时间。
      isLock = redLock.tryLock(500, 10000, TimeUnit.MILLISECONDS);
      if (isLock) {
        //TODO if get lock success, do something;
      }
    } catch (Exception e) {
    } finally {
      // 无论如何, 最后都要解锁
      redLock.unlock();
    }
  }

}
