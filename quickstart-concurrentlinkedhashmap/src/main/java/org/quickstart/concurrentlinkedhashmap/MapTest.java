package org.quickstart.concurrentlinkedhashmap;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.Weighers;
import com.sun.javafx.geom.Edge;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import org.junit.Test;
import sun.security.provider.certpath.Vertex;

/**
 * @author youngzil@163.com
 * @description TODO
 * @createTime 2019-07-10 23:57
 */
public class MapTest {

  @Test
  public void testMap() {

    // ConcurrentMap<K, V> cache = new ConcurrentLinkedHashMap.Builder<K, V>()
    // //设置Map的最大容量（权重）。
    // //如果Map中每个元素的权重都是1的话，那么就和普通HashMap的容量是一个意思。
    // //例如：A元素权重为1，B元素权重为2，那么A+B总权重是3，那么相当于占了3个容量。
    // .maximumWeightedCapacity(1000)
    // .weigher(Weighers.singleton()) //设置权重管理器，每个元素权重为1
    // .build();

    // Map中的元素是set，Weigher通过set的size来计算每一个元素的权重
    ConcurrentMap<Vertex, Set<Edge>> graphCache =
        new ConcurrentLinkedHashMap.Builder<Vertex, Set<Edge>>().maximumWeightedCapacity(1000).weigher(Weighers.<Edge>set()).build();

  }

}
