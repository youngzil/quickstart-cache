package org.quickstart.redis.test;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>描述: [功能描述] </p >
 *
 * @author yangzl
 * @version v1.0
 * @date 2020/8/10 11:30
 */
public class BallThThreadFactory implements ThreadFactory {

  private final AtomicLong threadIndex = new AtomicLong(0);
  private final String threadNamePrefix;
  private final boolean daemon;

  public BallThThreadFactory(final String threadNamePrefix) {
    this(threadNamePrefix, false);
  }

  public BallThThreadFactory(final String threadNamePrefix, boolean daemon) {
    this.threadNamePrefix = threadNamePrefix;
    this.daemon = daemon;
  }

  @Override
  public Thread newThread(Runnable r) {
    Thread thread = new Thread(r, threadNamePrefix + this.threadIndex.incrementAndGet());
    thread.setDaemon(daemon);
    return thread;
  }

}
