package org.quickstart.redis.replicator;

import com.moilioncircle.redis.replicator.RedisReplicator;
import com.moilioncircle.redis.replicator.Replicator;
import com.moilioncircle.redis.replicator.event.Event;
import com.moilioncircle.redis.replicator.event.EventListener;
import com.moilioncircle.redis.replicator.event.PostRdbSyncEvent;
import com.moilioncircle.redis.replicator.event.PreRdbSyncEvent;
import com.moilioncircle.redis.replicator.io.RawByteListener;
import com.moilioncircle.redis.replicator.rdb.skip.SkipRdbVisitor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;

/**
 * @author Leon Chen
 * @since 2.1.0
 */
public class RdbBackupExample {

    public static void main(String[] args) throws IOException, URISyntaxException {

        String rdbFilePath = "/Users/lengfeng/Desktop/redisRDB/dump3.rdb";
        final OutputStream out = new BufferedOutputStream(new FileOutputStream(new File(rdbFilePath)));
        final RawByteListener rawByteListener = new RawByteListener() {
            @Override
            public void handle(byte... rawBytes) {
                try {
                    out.write(rawBytes);
                } catch (IOException ignore) {
                }
            }
        };

        //save rdb from remote server
        // Replicator replicator = new RedisReplicator("redis://127.0.0.1:6379");
        // Replicator replicator = new RedisReplicator("redis://172.16.48.179:7000");
        // Replicator replicator = new RedisReplicator("redis://172.16.48.180:7002");
        Replicator replicator = new RedisReplicator("redis://172.16.48.181:7004");
        replicator.setRdbVisitor(new SkipRdbVisitor(replicator));
        replicator.addEventListener(new EventListener() {
            @Override
            public void onEvent(Replicator replicator, Event event) {
                if (event instanceof PreRdbSyncEvent) {
                    replicator.addRawByteListener(rawByteListener);
                }

                if (event instanceof PostRdbSyncEvent) {
                    replicator.removeRawByteListener(rawByteListener);
                    try {
                        out.close();
                        replicator.close();
                    } catch (IOException ignore) {
                    }
                }
            }
        });
        replicator.open();

        //check rdb file
        replicator = new RedisReplicator("redis:" + rdbFilePath);
        replicator.addEventListener(new EventListener() {
            @Override
            public void onEvent(Replicator replicator, Event event) {
                System.out.println(event);
            }
        });
        replicator.open();
    }

}
