/**
 * 项目名称：quickstart-memcached 
 * 文件名：TestTraversalMemcached.java
 * 版本信息：
 * 日期：2017年11月18日
 * Copyright asiainfo Corporation 2017
 * 版权所有 *
 */
package org.quickstart.cache.memcached.memcached.java.client.v3x;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;

/**
 * TestTraversalMemcached
 * 
 * @author：youngzil@163.com
 * @2017年11月18日 下午2:24:45
 * @since 1.0
 */
public class TestTraversalMemcached {

    public static void main(String[] args) {
        // memcached should be running on port 11211 but NOT on 11212

        String[] servers = {"10.19.13.53:20001"};
        // String[] servers = { "localhost:11111" };
        SockIOPool pool = SockIOPool.getInstance();
        pool.setServers(servers);
        pool.setFailover(true);
        pool.setInitConn(10);
        pool.setMinConn(5);
        pool.setMaxConn(250);
        // pool.setMaintSleep( 30 );
        pool.setNagle(false);
        pool.setSocketTO(3000);
        pool.setAliveCheck(true);
        pool.initialize();

        MemCachedClient mcc = new MemCachedClient();

        /* 在java memcached client documentation中没有提共遍历memcache所有key的方法。但是提供了两个方法statsItems和statsCacheDump，
        通过statsitems可以获取memcache中有多少个item，每个item上有多少个key，而statsCacheDump可以获取每个item上各个key的信息（key的名称，大小，以及有效期）。*/

        // 遍历statsItems 获取items:2:number=14
        Set<String> keys = new HashSet<>();
        Map<String, Map<String, String>> statsItems = mcc.statsItems();
        if (null != statsItems) {

            // 循环所有的server
            for (Entry<String/*server address*/, Map<String, String>> statsItem : statsItems.entrySet()) {
                String server = statsItem.getKey();// ip:port
                System.out.println("server=" + server);

                // statsItemsSub格式是：key=items+:+数字+:+字符串，value=数字，如items:2:number=14
                Map<String, String> statsItemsSub = statsItem.getValue();

                if (null != statsItemsSub) {

                    // 循环每个server的statsItems
                    for (Entry<String, String> statsItemsSubEntry : statsItemsSub.entrySet()) {

                        String statsItemsSubKey = statsItemsSubEntry.getKey();
                        int items_number = Integer.parseInt(statsItemsSubEntry.getValue().trim());

                        Map<String, Map<String, String>> statsCacheDumps = mcc.statsCacheDump(new String[] {server}, Integer.parseInt(statsItemsSubKey.split(":")[1].trim()), items_number);

                        if (null != statsCacheDumps) {
                            for (Entry<String, Map<String, String>> statsCacheDump : statsCacheDumps.entrySet()) {
                                Map<String, String> statsCacheDumpSubs = statsCacheDump.getValue();
                                if (null != statsCacheDumpSubs) {
                                    for (Entry<String, String> statsCacheDumpSub : statsCacheDumpSubs.entrySet()) {
                                        String statsCacheDumpSubKey = statsCacheDumpSub.getKey();
                                        String statsCacheDumpSubValue = statsCacheDumpSub.getValue();

                                        System.out.println(statsCacheDumpSubKey + "==" + statsCacheDumpSubValue);// key是中文被编码了,是客户端在set之前编码的，服务端中文key存的是密文

                                        keys.add(statsCacheDumpSubKey);
                                        // keylist.put(URLDecoder.decode(statsCacheDumpsub_key, "UTF-8"), new KeysBean(server,
                                        // Long.parseLong(statsCacheDumpsub_key_value.substring(1, statsCacheDumpsub_key_value.indexOf("b;") - 1).trim()),
                                        // Long.parseLong(statsCacheDumpsub_key_value.substring(statsCacheDumpsub_key_value.indexOf("b;") + 2, statsCacheDumpsub_key_value.indexOf("s]") - 1).trim())));

                                    }
                                }

                            }
                        }

                    }
                }

            }
        }

        System.out.println("keys=" + keys);

    }

}
