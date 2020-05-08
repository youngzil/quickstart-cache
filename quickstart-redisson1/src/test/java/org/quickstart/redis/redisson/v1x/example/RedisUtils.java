/**
 * 项目名称：quickstart-redis 
 * 文件名：RedisUtils.java
 * 版本信息：
 * 日期：2017年11月20日
 * Copyright yangzl Corporation 2017
 * 版权所有 *
 */
package org.quickstart.redis.redisson.v1x.example;

import org.redisson.Config;
import org.redisson.Redisson;
import org.redisson.core.RAtomicLong;
import org.redisson.core.RBucket;
import org.redisson.core.RCountDownLatch;
import org.redisson.core.RDeque;
import org.redisson.core.RList;
import org.redisson.core.RLock;
import org.redisson.core.RMap;
import org.redisson.core.RQueue;
import org.redisson.core.RSet;
import org.redisson.core.RSortedSet;
import org.redisson.core.RTopic;

/**
 * RedisUtils Redis client的辅助工具类 用于连接Redis服务器 创建不同的Redis Server对应的客户端对象 http://blog.csdn.net/undergrowth/article/details/46301559
 * 
 * @author：youngzil@163.com
 * @2017年11月20日 下午2:58:35
 * @since 1.0
 */

public class RedisUtils {

    private static RedisUtils redisUtils;

    private RedisUtils() {}

    /**
     * 提供单例模式
     * 
     * @return
     */
    public static RedisUtils getInstance() {
        if (redisUtils == null)
            synchronized (RedisUtils.class) {
                if (redisUtils == null)
                    redisUtils = new RedisUtils();
            }
        return redisUtils;
    }

    /**
     * 使用config创建Redisson Redisson是用于连接Redis Server的基础类
     * 
     * @param config
     * @return
     */
    public Redisson getRedisson(Config config) {
        Redisson redisson = Redisson.create(config);
        System.out.println("成功连接Redis Server");
        return redisson;
    }

    /**
     * 使用ip地址和端口创建Redisson
     * 
     * @param ip
     * @param port
     * @return
     */
    public Redisson getRedisson(String ip, String port) {
        Config config = new Config();
        config.useSingleServer().setAddress(ip + ":" + port);
        Redisson redisson = Redisson.create(config);
        System.out.println("成功连接Redis Server" + "\t" + "连接" + ip + ":" + port + "服务器");
        return redisson;
    }

    /**
     * 关闭Redisson客户端连接
     * 
     * @param redisson
     */
    public void closeRedisson(Redisson redisson) {
        redisson.shutdown();
        System.out.println("成功关闭Redis Client连接");
    }

    /**
     * 获取字符串对象
     * 
     * @param redisson
     * @param t
     * @param objectName
     * @return
     */
    public <T> RBucket<T> getRBucket(Redisson redisson, String objectName) {
        RBucket<T> bucket = redisson.getBucket(objectName);
        return bucket;
    }

    /**
     * 获取Map对象
     * 
     * @param redisson
     * @param objectName
     * @return
     */
    public <K, V> RMap<K, V> getRMap(Redisson redisson, String objectName) {
        RMap<K, V> map = redisson.getMap(objectName);
        return map;
    }

    /**
     * 获取有序集合
     * 
     * @param redisson
     * @param objectName
     * @return
     */
    public <V> RSortedSet<V> getRSortedSet(Redisson redisson, String objectName) {
        RSortedSet<V> sortedSet = redisson.getSortedSet(objectName);
        return sortedSet;
    }

    /**
     * 获取集合
     * 
     * @param redisson
     * @param objectName
     * @return
     */
    public <V> RSet<V> getRSet(Redisson redisson, String objectName) {
        RSet<V> rSet = redisson.getSet(objectName);
        return rSet;
    }

    /**
     * 获取列表
     * 
     * @param redisson
     * @param objectName
     * @return
     */
    public <V> RList<V> getRList(Redisson redisson, String objectName) {
        RList<V> rList = redisson.getList(objectName);
        return rList;
    }

    /**
     * 获取队列
     * 
     * @param redisson
     * @param objectName
     * @return
     */
    public <V> RQueue<V> getRQueue(Redisson redisson, String objectName) {
        RQueue<V> rQueue = redisson.getQueue(objectName);
        return rQueue;
    }

    /**
     * 获取双端队列
     * 
     * @param redisson
     * @param objectName
     * @return
     */
    public <V> RDeque<V> getRDeque(Redisson redisson, String objectName) {
        RDeque<V> rDeque = redisson.getDeque(objectName);
        return rDeque;
    }

    /**
     * 此方法不可用在Redisson 1.2 中 在1.2.2版本中 可用
     * 
     * @param redisson
     * @param objectName
     * @return
     */
    /**
     * public <V> RBlockingQueue<V> getRBlockingQueue(Redisson redisson,String objectName){ RBlockingQueue rb=redisson.getBlockingQueue(objectName); return rb; }
     */

    /**
     * 获取锁
     * 
     * @param redisson
     * @param objectName
     * @return
     */
    public RLock getRLock(Redisson redisson, String objectName) {
        RLock rLock = redisson.getLock(objectName);
        return rLock;
    }

    /**
     * 获取原子数
     * 
     * @param redisson
     * @param objectName
     * @return
     */
    public RAtomicLong getRAtomicLong(Redisson redisson, String objectName) {
        RAtomicLong rAtomicLong = redisson.getAtomicLong(objectName);
        return rAtomicLong;
    }

    /**
     * 获取记数锁
     * 
     * @param redisson
     * @param objectName
     * @return
     */
    public RCountDownLatch getRCountDownLatch(Redisson redisson, String objectName) {
        RCountDownLatch rCountDownLatch = redisson.getCountDownLatch(objectName);
        return rCountDownLatch;
    }

    /**
     * 获取消息的Topic
     * 
     * @param redisson
     * @param objectName
     * @return
     */
    public <M> RTopic<M> getRTopic(Redisson redisson, String objectName) {
        RTopic<M> rTopic = redisson.getTopic(objectName);
        return rTopic;
    }

}
