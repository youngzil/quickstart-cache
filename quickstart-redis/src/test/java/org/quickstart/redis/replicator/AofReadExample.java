package org.quickstart.redis.replicator;

import com.moilioncircle.redis.replicator.Configuration;
import com.moilioncircle.redis.replicator.FileType;
import com.moilioncircle.redis.replicator.RedisReplicator;
import com.moilioncircle.redis.replicator.Replicator;
import com.moilioncircle.redis.replicator.cmd.Command;
import com.moilioncircle.redis.replicator.cmd.impl.HSetCommand;
import com.moilioncircle.redis.replicator.cmd.impl.SetCommand;
import com.moilioncircle.redis.replicator.event.Event;
import com.moilioncircle.redis.replicator.event.EventListener;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author Leon Chen
 * @since 2.1.0
 */
public class AofReadExample {

    @Test
    public void readFile() throws IOException {

        String aofFilePath = "/Users/lengfeng/Desktop/redisRDB/appendonly.aof";

        final Replicator replicator = new RedisReplicator(new File(aofFilePath), FileType.AOF, Configuration.defaultSetting());
        replicator.addEventListener(new EventListener() {
            @Override
            public void onEvent(Replicator replicator, Event event) {
                if (event instanceof Command) {
                    System.out.println(event);
                }

                if (event instanceof SetCommand) {
                    SetCommand setCommand = (SetCommand)event;
                    byte[] keyByte = setCommand.getKey();
                    byte[] valueByte = setCommand.getValue();

                    System.out.println("key=" + new String(keyByte) + ",value=" + new String(valueByte));
                } else if (event instanceof HSetCommand) {

                    HSetCommand hsetCommand = (HSetCommand)event;
                    byte[] keyByte = hsetCommand.getKey();
                    Map<byte[], byte[]> valueByte = hsetCommand.getFields();

                    String valueStr = "";
                    if (null != valueByte) {
                        for (Map.Entry<byte[], byte[]> entry : valueByte.entrySet()) {
                            valueStr += new String(entry.getKey()) + "=" + new String(entry.getValue()) + ",";
                        }
                    }

                    System.out.println("key=" + new String(keyByte) + ",value=" + valueStr);
                }
                // ...

            }
        });

        replicator.open();
    }

    @SuppressWarnings("resource")
    public static void readInputStream() throws IOException {
        final Replicator replicator =
            new RedisReplicator(AofReadExample.class.getResourceAsStream("/appendonly1.aof"), FileType.AOF, Configuration.defaultSetting());
        replicator.addEventListener(new EventListener() {
            @Override
            public void onEvent(Replicator replicator, Event event) {
                if (event instanceof Command) {
                    System.out.println(event);
                }
            }
        });

        replicator.open();
    }
}
