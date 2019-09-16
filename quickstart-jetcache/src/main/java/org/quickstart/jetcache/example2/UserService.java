package org.quickstart.jetcache.example2;

import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CacheUpdate;
import com.alicp.jetcache.anno.Cached;
import com.alicp.jetcache.anno.CreateCache;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion.User;

/**
 * @author youngzil@163.com
 * @description TODO
 * @createTime 2019/9/16 22:13
 */
public interface UserService {

  @Cached(expire = 3600, cacheType = CacheType.REMOTE)
  User getUserById(long userId);

  @Cached(name="userCache-", key="#userId", expire = 3600)
  User getUserById2(long userId);

  @CacheUpdate(name="userCache-", key="#user.userId", value="#user")
  void updateUser(User user);

  @CacheInvalidate(name="userCache-", key="#userId")
  void deleteUser(long userId);




}
