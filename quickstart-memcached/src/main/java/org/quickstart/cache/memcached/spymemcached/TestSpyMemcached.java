/**
 * 项目名称：quickstart-memcached 
 * 文件名：TestSpyMemcached.java
 * 版本信息：
 * 日期：2017年3月18日
 * Copyright asiainfo Corporation 2017
 * 版权所有 *
 */
package org.quickstart.cache.memcached.spymemcached;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import net.spy.memcached.MemcachedClient;

/**
 * TestSpyMemcached
 * 
 * @author：yangzl@asiainfo.com
 * @2017年3月18日 下午9:11:49
 * @version 1.0
 */
public class TestSpyMemcached {

    public static void main(String[] args) {

        // 保存对象

        try {

            /* 建立MemcachedClient 实例，并指定memcached服务的IP地址和端口号 */

            InetSocketAddress socketAddress1 = new InetSocketAddress("10.19.13.53", 20001);
            // InetSocketAddress socketAddress1 = new InetSocketAddress("192.168.1.47", 11211);
            // InetSocketAddress socketAddress2 = new InetSocketAddress("192.168.1.47", 12000);
            // InetSocketAddress socketAddress3 = new InetSocketAddress("192.168.1.47", 12001);

            List<InetSocketAddress> addressList = new ArrayList<InetSocketAddress>();
            addressList.add(socketAddress1);
            // addressList.add(socketAddress2);
            // addressList.add(socketAddress3);

            // MemcachedClient mc = new MemcachedClient(socketAddress1,socketAddress2,socketAddress3);
            MemcachedClient mc = new MemcachedClient(addressList);

            Future<Boolean> b = null;

            /* 将key值，过期时间(秒)和要缓存的对象set到memcached中 */

            b = mc.set("neea:testDaF:ksIdno", 900, "ssss");

            if (b.get().booleanValue()) {

                // b= mc.delete("neea:testDaF:ksIdno");
                System.out.println(b.get().booleanValue());

                /* 按照key值从memcached中查找缓存，不存在则返回null */
                Object o = mc.get("neea:testDaF:ksIdno");

                System.out.println("neea:testDaF:ksIdno=" + o);

            }

            mc.shutdown();

        } catch (Exception ex) {

            ex.printStackTrace();

        }

    }

}
