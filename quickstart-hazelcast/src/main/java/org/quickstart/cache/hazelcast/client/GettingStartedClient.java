/**
 * 项目名称：quickstart-hazelcast 
 * 文件名：GettingStartedClient.java
 * 版本信息：
 * 日期：2018年4月20日
 * Copyright asiainfo Corporation 2018
 * 版权所有 *
 */
package org.quickstart.cache.hazelcast.client;

import java.util.Queue;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

/**
 * GettingStartedClient
 * 
 * @author：youngzil@163.com
 * @2018年4月20日 下午2:46:40
 * @since 1.0
 */
public class GettingStartedClient {

    public static void main(String[] args) {
        ClientConfig clientConfig = new ClientConfig();
        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
        IMap map = client.getMap("customers");
        System.out.println("Customer with key 1: " + map.get(1));
        System.out.println("Customer with key 2: " + map.get(2));
        System.out.println("Customer with key 3: " + map.get(3));
        System.out.println("Map Size:" + map.size());
        
        Queue<String> queueCustomers = client.getQueue("customers");
        System.out.println("Second customer: " + queueCustomers.peek());
        System.out.println("Queue size: " + queueCustomers.size());
        
    }

}
