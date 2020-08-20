package org.quickstart.redis.test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>描述: [功能描述] </p >
 *
 * @author yangzl
 * @version v1.0
 * @date 2020/8/10 11:42
 */
public class BallThThread implements Runnable {

  ReentrantLock lock;
  BallEntity ballEntity;
  CyclicBarrier cyclicBarrier;

  public BallThThread(ReentrantLock lock, BallEntity ballEntity, CyclicBarrier cyclicBarrier) {
    this.lock = lock;
    this.ballEntity = ballEntity;
    this.cyclicBarrier = cyclicBarrier;
  }


  public void get() {

    try {
      this.cyclicBarrier.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (BrokenBarrierException e) {
      e.printStackTrace();
    }

    if (this.lock.tryLock()) {
      System.out.println("抢到了球的线程是：" + Thread.currentThread().getName());
      this.lock.unlock();
    } else {
      System.out.println("很遗憾，没有抢到球，线程是：" + Thread.currentThread().getName());
    }

  }

  @Override
  public void run() {
    get();
  }

}
