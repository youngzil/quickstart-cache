package org.quickstart.redis.replicator;

import com.moilioncircle.redis.replicator.Configuration;
import com.moilioncircle.redis.replicator.FileType;
import com.moilioncircle.redis.replicator.RedisReplicator;
import com.moilioncircle.redis.replicator.Replicator;
import com.moilioncircle.redis.replicator.event.Event;
import com.moilioncircle.redis.replicator.event.EventListener;
import com.moilioncircle.redis.replicator.rdb.DefaultRdbVisitor;
import com.moilioncircle.redis.replicator.rdb.datatype.KeyStringValueHash;
import com.moilioncircle.redis.replicator.rdb.datatype.KeyStringValueList;
import com.moilioncircle.redis.replicator.rdb.datatype.KeyStringValueModule;
import com.moilioncircle.redis.replicator.rdb.datatype.KeyStringValueSet;
import com.moilioncircle.redis.replicator.rdb.datatype.KeyStringValueStream;
import com.moilioncircle.redis.replicator.rdb.datatype.KeyStringValueString;
import com.moilioncircle.redis.replicator.rdb.datatype.KeyStringValueZSet;
import com.moilioncircle.redis.replicator.rdb.datatype.Module;
import com.moilioncircle.redis.replicator.rdb.datatype.Stream;
import com.moilioncircle.redis.replicator.rdb.datatype.ZSetEntry;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class RdbReadExample {

    @Test
    public void testReadRDBFile() throws URISyntaxException, IOException {
        Replicator replicator = new RedisReplicator("redis:/Users/lengfeng/Desktop/redisRDB/dump_6301.rdb");

        replicator.setRdbVisitor(new DefaultRdbVisitor(replicator));

        replicator.open();
    }

    @Test
    public void readFile() throws IOException {

        // String rdbFilePath = "/Users/lengfeng/Desktop/redisRDB/dump_6301.rdb";
        String rdbFilePath = "/Users/lengfeng/Desktop/redisRDB/dump3.rdb";
        AtomicInteger keyCount = new AtomicInteger();

        final Replicator replicator = new RedisReplicator(new File(rdbFilePath), FileType.RDB, Configuration.defaultSetting());

        replicator.addEventListener(new EventListener() {
            @Override
            public void onEvent(Replicator replicator, Event event) {
                // System.out.println("replicator=" + replicator);
                // System.out.println("event=" + event);

                System.out.println("keyCount=" + keyCount.incrementAndGet());
                // 对应Redis中的7种数据类型
                if (event instanceof KeyStringValueString) {
                    KeyStringValueString stringEvent = (KeyStringValueString)event;
                    byte[] keyByte = stringEvent.getKey();
                    byte[] valueByte = stringEvent.getValue();

                    System.out.println("key=" + new String(keyByte) + ",value=" + new String(valueByte));
                }

                if (event instanceof KeyStringValueHash) {
                    KeyStringValueHash hashEvent = (KeyStringValueHash)event;
                    byte[] keyByte = hashEvent.getKey();
                    Map<byte[], byte[]> valueByte = hashEvent.getValue();

                    String valueStr = "";
                    if (null != valueByte) {
                        for (Map.Entry<byte[], byte[]> entry : valueByte.entrySet()) {
                            valueStr += new String(entry.getKey()) + "=" + new String(entry.getValue()) + ",";
                        }
                    }

                    System.out.println("key=" + new String(keyByte) + ",value=" + valueStr);
                }

                if (event instanceof KeyStringValueList) {
                    KeyStringValueList listEvent = (KeyStringValueList)event;
                    byte[] keyByte = listEvent.getKey();
                    List<byte[]> valueByte = listEvent.getValue();

                    System.out.println("key=" + new String(keyByte) + ",value=");
                }

                if (event instanceof KeyStringValueSet) {
                    KeyStringValueSet setEvent = (KeyStringValueSet)event;
                    byte[] keyByte = setEvent.getKey();
                    Set<byte[]> valueByte = setEvent.getValue();

                    System.out.println("key=" + new String(keyByte) + ",value=");
                }

                if (event instanceof KeyStringValueZSet) {
                    KeyStringValueZSet zsetEvent = (KeyStringValueZSet)event;
                    byte[] keyByte = zsetEvent.getKey();
                    Set<ZSetEntry> valueByte = zsetEvent.getValue();

                    System.out.println("key=" + new String(keyByte) + ",value=");
                }

                if (event instanceof KeyStringValueModule) {
                    KeyStringValueModule moduleEvent = (KeyStringValueModule)event;
                    byte[] keyByte = moduleEvent.getKey();
                    Module valueByte = moduleEvent.getValue();

                    System.out.println("key=" + new String(keyByte) + ",value=");
                }

                if (event instanceof KeyStringValueStream) {
                    KeyStringValueStream streamEvent = (KeyStringValueStream)event;
                    byte[] keyByte = streamEvent.getKey();
                    Stream valueByte = streamEvent.getValue();

                    System.out.println("key=" + new String(keyByte) + ",value=");
                }

            }
        });

        replicator.open();
    }

    @Test
    public void readInputStream() throws IOException {
        final Replicator replicator =
            new RedisReplicator(RdbReadExample.class.getResourceAsStream("/dumpV7.rdb"), FileType.RDB, Configuration.defaultSetting());
        replicator.addEventListener(new EventListener() {
            @Override
            public void onEvent(Replicator replicator, Event event) {
                System.out.println(event);
            }
        });

        replicator.open();
    }

}