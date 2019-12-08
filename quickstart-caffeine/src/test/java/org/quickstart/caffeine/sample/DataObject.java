package org.quickstart.caffeine.sample;

import lombok.Getter;
import lombok.Setter;

/**
 * @author youngzil@163.com
 * @description TODO
 * @createTime 2019-07-06 08:11
 */
@Getter
@Setter
public class DataObject {

  private String data;

  private static int objectCounter = 0;

  public DataObject(String data) {
    this.data = data;
  }

  public DataObject() {

  }

  // standard constructors/getters

  public static DataObject get(String data) {
    objectCounter++;
    return new DataObject(data);
  }

}
