package org.quickstart.ehcache.cluster;

import static org.ehcache.config.builders.CacheManagerBuilder.newCacheManager;
import static org.slf4j.LoggerFactory.getLogger;

import java.net.URL;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.Configuration;
import org.ehcache.xml.XmlConfiguration;
import org.slf4j.Logger;

public class ClusteredXML {

  // 先按照serverDeploy部署集群server
  // /Users/yangzl/Downloads/ehcache-clustered-3.5.2-kit/server/bin/start-tc-server.sh -f ./tc-config.xml
  private static final Logger LOGGER = getLogger(ClusteredXML.class);

  public static void main(String[] args) {
    LOGGER.info("Creating clustered cache manager from XML");
    final URL myUrl = ClusteredXML.class.getResource("/ehcache-cluster.xml");
    Configuration xmlConfig = new XmlConfiguration(myUrl);
    try (CacheManager cacheManager = newCacheManager(xmlConfig)) {
      cacheManager.init();

      Cache<Long, String> basicCache = cacheManager.getCache("basicCache", Long.class, String.class);

      LOGGER.info("Getting from cache");
      String value = basicCache.get(1L);
      LOGGER.info("Retrieved '{}'", value);

      LOGGER.info("Closing cache manager");
    }

    LOGGER.info("Exiting");
  }
}
