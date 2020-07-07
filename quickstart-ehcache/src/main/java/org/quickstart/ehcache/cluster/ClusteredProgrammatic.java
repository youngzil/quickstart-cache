package org.quickstart.ehcache.cluster;

import static java.net.URI.create;
import static org.ehcache.clustered.client.config.builders.ClusteredResourcePoolBuilder.clusteredDedicated;
import static org.ehcache.clustered.client.config.builders.ClusteringServiceConfigurationBuilder.cluster;
import static org.ehcache.config.builders.CacheConfigurationBuilder.newCacheConfigurationBuilder;
import static org.ehcache.config.builders.CacheManagerBuilder.newCacheManagerBuilder;
import static org.ehcache.config.builders.ResourcePoolsBuilder.heap;
import static org.ehcache.config.units.MemoryUnit.MB;
import static org.slf4j.LoggerFactory.getLogger;

import java.net.URI;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.slf4j.Logger;

public class ClusteredProgrammatic {

  // 先按照serverDeploy部署集群server
  // /Users/yangzl/Downloads/ehcache-clustered-3.5.2-kit/server/bin/start-tc-server.sh -f ./tc-config.xml
  private static final Logger LOGGER = getLogger(ClusteredProgrammatic.class);

  public static void main(String[] args) {
    LOGGER.info("Creating clustered cache manager");
    final URI uri = create("terracotta://localhost:9510/clustered");
    try (CacheManager cacheManager = newCacheManagerBuilder()//
        .with(cluster(uri).autoCreate().defaultServerResource("default-resource"))//
        .withCache("basicCache", //
            newCacheConfigurationBuilder(Long.class, String.class, //
                heap(100).offheap(1, MB).with(clusteredDedicated(5, MB))))//
        .build(true)) {
      Cache<Long, String> basicCache = cacheManager.getCache("basicCache", Long.class, String.class);

      LOGGER.info("Putting to cache");
      basicCache.put(1L, "da one!");

      LOGGER.info("Closing cache manager");
    }

    LOGGER.info("Exiting");
  }
}
