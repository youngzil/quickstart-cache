package org.quickstart.cache.redis.jedis.lock;

public class RedisLockTest {

  private static volatile int add = 10;

  public static void main(String[] args) {

    Runnable handler = new Runnable() {
      @Override
      public void run() {
        while (add > 0) {
          System.out.println(Thread.currentThread().toString() + "  ————————  add@" + add);
          add--;
          try {
            Thread.sleep(500);
          } catch (InterruptedException e) {
          }
        }
      }
    };

    new Thread(handler).start();
    new Thread(handler).start();
    new Thread(handler).start();

  }
}
