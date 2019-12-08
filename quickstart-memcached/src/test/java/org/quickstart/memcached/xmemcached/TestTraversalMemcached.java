/**
 * 项目名称：quickstart-memcached 
 * 文件名：TestTraversalMemcached.java
 * 版本信息：
 * 日期：2017年11月18日
 * Copyright yangzl Corporation 2017
 * 版权所有 *
 */
package org.quickstart.memcached.xmemcached;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.KeyIterator;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedException;

/**
 * TestTraversalMemcached
 * 
 * @author：youngzil@163.com
 * @2017年11月18日 下午2:45:33
 * @since 1.0
 */
public class TestTraversalMemcached {

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

            Collection<InetSocketAddress> addrList = memcachedClient.getAvailableServers();

            if (null != addrList) {
                for (InetSocketAddress address : addrList) {

                    System.out.println("--------------------------------------------------------------------------");
                    System.out.println("address=" + address);

                    System.out.println("keys=" + address);
                    KeyIterator keys = memcachedClient.getKeyIterator(address);
                    while (keys.hasNext()) {
                        String key = keys.next();
                        System.out.println(key);
                    }

                    Map<InetSocketAddress, Map<String, String>> statsItems = memcachedClient.getStats();
                    System.out.println(statsItems);

                    Map<String, String> statsMap = memcachedClient.stats(address);
                    System.out.println(statsMap);

                    // memcachedClient.getStatsByItem(itemName)

                }
            }

            // close memcached client
            memcachedClient.shutdown();

        } catch (MemcachedException e) {

            System.err.println("MemcachedClient operation fail");

            e.printStackTrace();

        } catch (TimeoutException e) {

            System.err.println("MemcachedClient operation timeout");

            e.printStackTrace();

        } catch (InterruptedException e) {

            System.err.println("Shutdown MemcachedClient fail");

            e.printStackTrace();

        } catch (IOException e) {

            System.err.println("Shutdown MemcachedClient fail");

            e.printStackTrace();

        }

    }

}
