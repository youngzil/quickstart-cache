package org.quickstart.ehcache.basic;

import static org.ehcache.config.builders.CacheManagerBuilder.newCacheManager;
import static org.slf4j.LoggerFactory.getLogger;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.Configuration;
import org.ehcache.xml.XmlConfiguration;
import org.slf4j.Logger;

public class BasicXML {

  private static final Logger LOGGER = getLogger(BasicXML.class);

  public static void main(String[] args) {
    LOGGER.info("Creating cache manager via XML resource");
    Configuration xmlConfig = new XmlConfiguration(BasicXML.class.getResource("/ehcache-basic.xml"));
    try (CacheManager cacheManager = newCacheManager(xmlConfig)) {
      cacheManager.init();

      Cache<Long, String> basicCache = cacheManager.getCache("basicCache", Long.class, String.class);

      LOGGER.info("Putting to cache");
      basicCache.put(1L, "da one!");
      String value = basicCache.get(1L);
      LOGGER.info("Retrieved '{}'", value);

      LOGGER.info("Closing cache manager");
    }

    LOGGER.info("Exiting");
  }
}
