package org.quickstart.redis.test;

/**
 * <p>描述: [功能描述] </p >
 *
 * @author yangzl
 * @version v1.0
 * @date 2020/8/10 11:40
 */
public class BallEntity {

  private String code;

  public BallEntity(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Override
  public String toString() {
    return "BallEntity{" +
        "code='" + code + '\'' +
        '}';
  }
}
