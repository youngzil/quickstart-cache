package org.quickstart.redis.jedis.lock;


// setnx方式配置lock测试类
//可以把方法改为lock_2 lock_3，lock_3是watch加Transaction的方式
public class RedisLockTest2 {

  private static volatile int add = 10;

  public static void main(String[] args) {

    Runnable handler = new Runnable() {
      @Override
      public void run() {
        RedisLock mylock = new RedisLock("testlock1");
        if (mylock.lock(300000)) {
          while (add > 0) {
            System.out.println(Thread.currentThread().toString() + "   ————————   add@" + add);
            add--;
            try {
              Thread.sleep(500);
            } catch (InterruptedException e) {
            }
          }
        }
        mylock.unlock();
      }
    };

    new Thread(handler).start();
    new Thread(handler).start();
    new Thread(handler).start();

  }

}
