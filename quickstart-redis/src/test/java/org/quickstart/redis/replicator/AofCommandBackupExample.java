package org.quickstart.redis.replicator;

import com.moilioncircle.redis.replicator.RedisReplicator;
import com.moilioncircle.redis.replicator.Replicator;
import com.moilioncircle.redis.replicator.cmd.Command;
import com.moilioncircle.redis.replicator.event.Event;
import com.moilioncircle.redis.replicator.event.EventListener;
import com.moilioncircle.redis.replicator.event.PostRdbSyncEvent;
import com.moilioncircle.redis.replicator.io.RawByteListener;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Leon Chen
 * @since 2.1.0
 */
@SuppressWarnings("resource")
public class AofCommandBackupExample {
    public static void main(String[] args) throws IOException, URISyntaxException {

        String aofFilePath = "/Users/lengfeng/Desktop/redisRDB/appendonly.aof";

        final OutputStream out = new BufferedOutputStream(new FileOutputStream(new File(aofFilePath)));
        final RawByteListener rawByteListener = new RawByteListener() {
            @Override
            public void handle(byte... rawBytes) {
                try {
                    out.write(rawBytes);
                } catch (IOException ignore) {
                }
            }
        };

        //save 1000 records commands
        // Replicator replicator = new RedisReplicator("redis://127.0.0.1:6379");
        // Replicator replicator = new RedisReplicator("redis://172.16.48.179:7000");
        // Replicator replicator = new RedisReplicator("redis://172.16.48.180:7002");
        Replicator replicator = new RedisReplicator("redis://172.16.48.181:7004");
        final AtomicInteger acc = new AtomicInteger(0);
        replicator.addEventListener(new EventListener() {
            @Override
            public void onEvent(Replicator replicator, Event event) {
                if (event instanceof PostRdbSyncEvent) {
                    replicator.addRawByteListener(rawByteListener);
                }
                if (event instanceof Command) {
                    if (acc.incrementAndGet() == 1000) {
                        try {
                            out.close();
                            replicator.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        replicator.open();

        //check aof file
        replicator = new RedisReplicator("redis:" + aofFilePath);
        replicator.addEventListener(new EventListener() {
            @Override
            public void onEvent(Replicator replicator, Event event) {
                System.out.println(event);
            }
        });
        replicator.open();
    }
}
