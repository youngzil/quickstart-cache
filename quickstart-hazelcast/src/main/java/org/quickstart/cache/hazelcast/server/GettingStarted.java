/**
 * 项目名称：quickstart-hazelcast 
 * 文件名：GettingStarted.java
 * 版本信息：
 * 日期：2018年4月20日
 * Copyright yangzl Corporation 2018
 * 版权所有 *
 */
package org.quickstart.cache.hazelcast.server;

import java.util.Map;
import java.util.Queue;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

/**
 * GettingStarted
 * 
 * https://blog.csdn.net/wangyangzhizhou/article/details/52529067
 * 
 * @author：youngzil@163.com
 * @2018年4月20日 下午2:42:06
 * @since 1.0
 */
public class GettingStarted {

    /*
     * 执行两次，是在两个不同的JVM中，这样会启动两个Hazelcast 节点（端口号自动分配）作为一个集群
     * 这样会启动两个Hazelcast 节点（端口号自动分配）作为一个集群。给其中一个节点的分布式Map中添加数据， 可以在多个节点同时取到数据。
    将多个Hazelcast节点部署在多个JVM中，即可。
     */

    public static void main(String[] args) {
        Config config = new Config();
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
//        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);
        Map<Integer, String> customers = hazelcastInstance.getMap("customers");
        customers.put(1, "Joe");
        customers.put(2, "Ali");
        customers.put(3, "Avi");
        System.out.println("Customer with key 1: " + customers.get(1));
        System.out.println("Map Size:" + customers.size());
        Queue<String> queueCustomers = hazelcastInstance.getQueue("customers");
        queueCustomers.offer("Tom");
        queueCustomers.offer("Mary");
        queueCustomers.offer("Jane");
        System.out.println("First customer: " + queueCustomers.poll());
        System.out.println("Second customer: " + queueCustomers.peek());
        System.out.println("Queue size: " + queueCustomers.size());
    }

}
