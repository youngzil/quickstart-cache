package org.quickstart.cache.redis.lettuce;

import io.lettuce.core.RedisClient;
import io.lettuce.core.pubsub.RedisPubSubListener;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import io.lettuce.core.pubsub.api.sync.RedisPubSubCommands;
import java.io.IOException;

/**
 * @author youngzil@163.com
 * @description TODO
 * @createTime 2019-07-06 23:49
 */
public class PubSubApi {
  public static void main(String[] args) throws IOException {
    RedisClient client = RedisClient.create("redis://localhost");

    // connection
    RedisPubSubListener<String, String> listener = new RedisPubSubListener<String, String>() {
      @Override
      public void message(String channel, String message) {
        System.out.println("message: " + channel + ", " + message);
      }

      @Override
      public void message(String pattern, String channel, String message) {
        System.out.println("message: " + pattern + ", " + channel + ", " + message);
      }

      @Override
      public void psubscribed(String pattern, long count) {
        System.out.println("psub: " + pattern + ", " + count);
      }

      @Override
      public void punsubscribed(String pattern, long count) {
        System.out.println("punsub: " + pattern + ", " + count);
      }

      @Override
      public void subscribed(String channel, long count) {
        System.out.println("sub: " + channel + ", " + count);
      }

      @Override
      public void unsubscribed(String channel, long count) {
        System.out.println("ubsub: " + channel + ", " + count);
      }
    };

    StatefulRedisPubSubConnection<String, String> pubSubConnection = client.connectPubSub();
    RedisPubSubCommands<String, String> sync = pubSubConnection.sync();
    RedisPubSubAsyncCommands<String, String> async = pubSubConnection.async();
    pubSubConnection.addListener(listener);

    // Subscriber
    // sync.subscribe("channel");
    // async.subscribe("channel");

    /*try {
      Thread.sleep(10000L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }*/

    // Publisher
    sync.publish("channel", "Hello, Redis!");
    // async.publish("channel", "Hello, Redis!");

    System.in.read();

    // pubSubConnection.close();
    // client.shutdown();
  }
}
