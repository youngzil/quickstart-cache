package org.quickstart.caffeine.sample;

import com.github.benmanes.caffeine.cache.CacheWriter;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalCause;
import org.junit.Test;

/**
 * @author yangzl
 * @description TODO
 * @createTime 2020/6/12 16:18
 */
public class CacheWriteTest {

  @Test
  public  void testWrite(){
    // 写入外部存储#
    // CacheWriter 方法可以将缓存中所有的数据写入到第三方。

    LoadingCache<String, Object> cache2 = Caffeine.newBuilder()
        .writer(new CacheWriter<String, Object>() {
          @Override public void write(String key, Object value) {
            // 写入到外部存储
          }
          @Override public void delete(String key, Object value, RemovalCause cause) {
            // 删除外部存储
          }
        })
        .build(key -> DataObject.get("Data for " + key));
    // 如果你有多级缓存的情况下，这个方法还是很实用。
    // 注意：CacheWriter不能与弱键或AsyncLoadingCache一起使用。

  }

}
