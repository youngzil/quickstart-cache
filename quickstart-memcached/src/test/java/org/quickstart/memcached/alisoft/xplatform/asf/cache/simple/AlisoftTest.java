/**
 * 项目名称：quickstart-memcached 
 * 文件名：AlisoftTest.java
 * 版本信息：
 * 日期：2017年3月18日
 * Copyright yangzl Corporation 2017
 * 版权所有 *
 */
package org.quickstart.memcached.alisoft.xplatform.asf.cache.simple;

import com.alisoft.xplatform.asf.cache.ICacheManager;
import com.alisoft.xplatform.asf.cache.IMemcachedCache;
import com.alisoft.xplatform.asf.cache.memcached.CacheUtil;
import com.alisoft.xplatform.asf.cache.memcached.MemcachedCacheManager;

/**
 * AlisoftTest
 * 
 * @author：youngzil@163.com
 * @2017年3月18日 下午8:45:35
 * @version 1.0
 */
public class AlisoftTest {

    public static void main(String[] args) {

        ICacheManager<IMemcachedCache> cacheManager = CacheUtil.getCacheManager(IMemcachedCache.class, MemcachedCacheManager.class.getName());
        cacheManager.setConfigFile("alisoft-xplatform-asf-cache/simple-memcached.xml");
        cacheManager.start();

        IMemcachedCache cache = cacheManager.getCache("mclient_0");

        cache.put("test1", "test11111");
        String str = (String) cache.get("test1");
        System.out.println(str);

    }

}
