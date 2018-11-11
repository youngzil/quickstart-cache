/**
 * 项目名称：quickstart-memcached 
 * 文件名：TestXMemcache.java
 * 版本信息：
 * 日期：2017年3月18日
 * Copyright asiainfo Corporation 2017
 * 版权所有 *
 */
package org.quickstart.cache.memcached.xmemcached;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;

/**
 * TestXMemcache
 * 
 * @author：youngzil@163.com
 * @2017年3月18日 下午9:02:44
 * @version 1.0
 */
public class TestXMemcached {

    public static void main(String[] args) {

        List<InetSocketAddress> addressList = new ArrayList<InetSocketAddress>();
        addressList.add(new InetSocketAddress("10.19.13.53", 20001));
        // addressList.add(new InetSocketAddress("192.168.1.47", 11211));
        // addressList.add(new InetSocketAddress("192.168.1.47", 12000));
        // addressList.add(new InetSocketAddress("192.168.1.47", 12001));
        MemcachedClientBuilder builder = new XMemcachedClientBuilder(addressList);

        // String addressStr = "192.168.1.47:11211 192.168.1.47:12000 192.168.1.47:12001";
        // MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses(addressStr));

        MemcachedClient memcachedClient;

        try {

            memcachedClient = builder.build();

            memcachedClient.set("hello", 0, "world");

            String value = memcachedClient.get("hello");

            System.out.println("hello=" + value);

            memcachedClient.delete("hello");

            value = memcachedClient.get("hello");

            System.out.println("hello=" + value);

            // close memcached client
            memcachedClient.shutdown();

        } catch (MemcachedException e) {

            System.err.println("MemcachedClient operation fail");

            e.printStackTrace();

        } catch (TimeoutException e) {

            System.err.println("MemcachedClient operation timeout");

            e.printStackTrace();

        } catch (InterruptedException e) {

            // ignore

        } catch (IOException e) {

            System.err.println("Shutdown MemcachedClient fail");

            e.printStackTrace();

        }

    }

}
