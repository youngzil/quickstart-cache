package org.quickstart.redis.replicator;

import com.moilioncircle.redis.replicator.Configuration;
import com.moilioncircle.redis.replicator.FileType;
import com.moilioncircle.redis.replicator.RedisReplicator;
import com.moilioncircle.redis.replicator.Replicator;
import com.moilioncircle.redis.replicator.event.Event;
import com.moilioncircle.redis.replicator.event.EventListener;

import java.io.File;
import java.io.IOException;

/**
 * @author Leon Chen
 * @since 2.1.0
 */
public class MixReadExample {

    @SuppressWarnings("resource")
    public static void readFile() throws IOException {
        final Replicator replicator =
            new RedisReplicator(new File("./src/test/resources/appendonly4.aof"), FileType.MIXED, Configuration.defaultSetting());

        replicator.addEventListener(new EventListener() {
            @Override
            public void onEvent(Replicator replicator, Event event) {
                System.out.println(event);
            }
        });

        replicator.open();
    }

    @SuppressWarnings("resource")
    public static void readInputStream() throws IOException {
        final Replicator replicator =
            new RedisReplicator(MixReadExample.class.getResourceAsStream("/appendonly4.aof"), FileType.MIXED, Configuration.defaultSetting());
        replicator.addEventListener(new EventListener() {
            @Override
            public void onEvent(Replicator replicator, Event event) {
                System.out.println(event);
            }
        });
        replicator.open();
    }
}
