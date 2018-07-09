/**
 * 项目名称：quickstart-memcached 
 * 文件名：TestTraversalMemcached.java
 * 版本信息：
 * 日期：2017年11月18日
 * Copyright asiainfo Corporation 2017
 * 版权所有 *
 */
package org.quickstart.cache.memcached.spymemcached;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.spy.memcached.MemcachedClient;

/**
 * TestTraversalMemcached
 * 
 * @author：yangzl@asiainfo.com
 * @2017年11月18日 下午2:32:27
 * @since 1.0
 */
public class TestTraversalMemcached {

    public static void main(String[] args) {

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

            mc.set("neea:testDaF:ksIdno", 900, "ssss");
            Object o = mc.get("neea:testDaF:ksIdno");

            System.out.println("neea:testDaF:ksIdno=" + o);

            // Set<String> stringSet = mc.listSaslMechanisms();
            // System.out.println(stringSet);

            /*Collection<SocketAddress> addrList = mc.getAvailableServers();
            if (null != addrList) {
                for (SocketAddress address : addrList) {
                    mc.getStats(address);
                }
            }*/

            Map<SocketAddress, Map<String, String>> statsItems = mc.getStats();
            System.out.println(statsItems);

            mc.shutdown();

        } catch (Exception ex) {

            ex.printStackTrace();

        }

    }

}
