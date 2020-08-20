package org.quickstart.redis.test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * <p>描述: [功能描述] </p >
 *
 * @author yangzl
 * @version v1.0
 * @date 2020/8/10 11:33
 */
public class Main {

  public static void main(String[] args) {

    int corePoolSize = 3;
    int maximumPoolSize = 3;
    long keepAliveTime = 60 * 1000;

    BlockingQueue<Runnable> acquireBallThreadPoolQueue = new LinkedBlockingQueue<Runnable>(20);

    ExecutorService executorService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
        keepAliveTime,
        TimeUnit.MILLISECONDS,
        acquireBallThreadPoolQueue,
        new BallThThreadFactory("AcquireBallThread_"));

    BallEntity ballEntity = new BallEntity("A");
    ReentrantLock lock = new ReentrantLock();
    CyclicBarrier cyclicBarrier = new CyclicBarrier(3);

    IntStream.range(0, 3).forEach(i -> executorService.submit(new BallThThread(lock, ballEntity, cyclicBarrier)));

    executorService.shutdown();

  }

}
