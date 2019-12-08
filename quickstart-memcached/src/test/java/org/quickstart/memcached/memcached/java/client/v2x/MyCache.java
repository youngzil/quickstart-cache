/**
 * 项目名称：quickstart-memcached 
 * 文件名：MyCache.java
 * 版本信息：
 * 日期：2017年3月18日
 * Copyright yangzl Corporation 2017
 * 版权所有 *
 */
package org.quickstart.memcached.memcached.java.client.v2x;

import java.util.Date;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

/**
 * MyCache
 * 
 * @author：youngzil@163.com
 * @2017年3月18日 下午6:56:19
 * @version 1.0
 */
public class MyCache {

    public static void main(String[] args) {

        MemCachedClient client = new MemCachedClient();
        String[] addr = new String[] {"192.168.1.47:11211", "192.168.1.47:12000", "192.168.1.47:12001"};
        Integer[] weights = {3};

        // 获取连接池实例
        SockIOPool pool = SockIOPool.getInstance();
        // 设置缓存服务器地址，可以设置多个实现分布式缓存
        pool.setServers(addr);
        pool.setWeights(weights);
        // 设置初始连接5
        pool.setInitConn(5);
        // 设置最小连接5
        pool.setMinConn(5);
        // 设置最大连接250
        pool.setMaxConn(250);
        // 设置每个连接最大空闲时间3个小时
        pool.setMaxIdle(1000 * 60 * 60 * 3);
        // 设置连接池维护线程的睡眠时间
        // 设置为0，维护线程不启动
        // 维护线程主要通过log输出socket的运行状况，监测连接数目及空闲等待时间等参数以控制连接创建和关闭。
        pool.setMaintSleep(30);
        // 设置是否使用Nagle算法，因为我们的通讯数据量通常都比较大（相对TCP控制数据）而且要求响应及时，因此该值需要设置为false（默认是true）
        pool.setNagle(false);
        // 设置socket的读取等待超时值
        pool.setSocketTO(3000);
        // 设置socket的连接等待超时值
        pool.setSocketConnectTO(0);
        // 设置完pool参数后最后调用该方法，启动pool。
        pool.initialize();

        // String [] s =pool.getServers();
        // client.setCompressEnable(true);
        // client.setCompressThreshold(1000 * 1024);

        // 将数据放入缓存
        boolean dd = client.set("test3", "test3");
        System.out.println(dd);
        // 将数据放入缓存,并设置失效时间
        Date date = new Date(2000000);
        client.set("test4", "test4", date);
        client.set("test5", "test5", date);

        // 删除缓存数据
        // client.delete("test1");

        // 获取缓存数据
        String str = (String) client.get("test1");
        System.out.println(str);

    }
}
