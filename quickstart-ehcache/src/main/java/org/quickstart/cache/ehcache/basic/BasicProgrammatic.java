package org.quickstart.cache.ehcache.basic;

import static org.ehcache.config.builders.CacheConfigurationBuilder.newCacheConfigurationBuilder;
import static org.ehcache.config.builders.CacheManagerBuilder.newCacheManagerBuilder;
import static org.ehcache.config.builders.ResourcePoolsBuilder.heap;
import static org.ehcache.config.units.MemoryUnit.MB;
import static org.slf4j.LoggerFactory.getLogger;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.core.EhcacheManager;
import org.slf4j.Logger;

public class BasicProgrammatic {
    private static final Logger LOGGER = getLogger(BasicProgrammatic.class);

    public static void main(String[] args) {
        LOGGER.info("Creating cache manager programmatically");
        
        
        
        CacheManagerBuilder<CacheManager> builder =  newCacheManagerBuilder();//空的实现，只是为了创建builder对象
        ResourcePoolsBuilder rpb = heap(100).offheap(1, MB);//设置资源池的配置
        CacheConfigurationBuilder ccb = newCacheConfigurationBuilder(Long.class, String.class, rpb);//缓存的配置
        
        //保存services（Set）、ConfigurationBuilder（caches（map类型，保存缓存配置），serviceConfigurations（list类型），ClassLoader（类加载器，定制缓存存放的加载器））、
        builder = builder.withCache("basicCache2", ccb);//真正的创建builder，
        
        //创建EhcacheManager中的CacheHolder的cache对象是关键，后面就是获取CacheHolder的cache进行put、get
        CacheManager cacheManager2 = builder.build(true);//创建的实际类型是EhcacheManager（caches（map类型，值是CacheHolder，CacheHolder中的cache才是保存的数据））
        
       // ( (EhcacheManager) cacheManager2 ).registerListener(listener);//可以注册CacheManagerListener，在cache注册和删除的时候，触发
        
        try (CacheManager cacheManager = newCacheManagerBuilder()//
                .withCache("basicCache", // 1
                        newCacheConfigurationBuilder(Long.class, String.class, heap(100).offheap(1, MB)))// 2
                .build(true)) {
            //其实是CacheHolder中的cache对象
            Cache<Long, String> basicCache = cacheManager.getCache("basicCache", Long.class, String.class);//获取之前创建的cache，传入key和value类型，是为了再次校验取出的cache类型是否匹配

            LOGGER.info("Putting to cache");
            basicCache.put(1L, "da one!");//CacheHolder中的cache对象中的store的put
            String value = basicCache.get(1L);
            LOGGER.info("Retrieved '{}'", value);

            LOGGER.info("Closing cache manager");
        }

        LOGGER.info("Exiting");
    }
}
