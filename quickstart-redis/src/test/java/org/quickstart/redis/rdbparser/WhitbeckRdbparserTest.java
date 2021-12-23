package org.quickstart.redis.rdbparser;

import net.whitbeck.rdbparser.AuxField;
import net.whitbeck.rdbparser.Entry;
import net.whitbeck.rdbparser.EntryType;
import net.whitbeck.rdbparser.KeyValuePair;
import net.whitbeck.rdbparser.RdbParser;
import net.whitbeck.rdbparser.ResizeDb;
import net.whitbeck.rdbparser.SelectDb;
import net.whitbeck.rdbparser.ValueType;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WhitbeckRdbparserTest {

    private BufferedWriter bfw = Files.newBufferedWriter(Paths.get("/Users/lengfeng/Desktop/rdb.log"));

    public WhitbeckRdbparserTest() throws IOException {
    }

    @Test
    public void testRdbParser() throws IOException, InterruptedException {

        RdbParser parser = new RdbParser(Paths.get("/Users/lengfeng/Desktop/redisRDB/dump_6301.rdb"));

        Entry e;
        while ((e = parser.readNext()) != null) {
            switch (e.getType()) {
                case AUX_FIELD:
                    bfw.write("----------------------------\n");
                    bfw.write(" EntryType=" + e.getType() + "\n");
                    AuxField auxField = (AuxField)e;
                    bfw.write(" getKey=" + new String(auxField.getKey()) + "\n");
                    bfw.write(" getValue=" + new String(auxField.getValue()) + "\n");
                    break;
                case EOF:
                    bfw.write("----------------------------\n");
                    bfw.write(" EntryType=" + e.getType() + "\n");
                    break;
                case RESIZE_DB:
                    bfw.write("----------------------------\n");
                    bfw.write(" EntryType=" + e.getType() + "\n");
                    ResizeDb resizeDb = (ResizeDb)e;
                    bfw.write(" getDbHashTableSize=" + resizeDb.getDbHashTableSize() + "\n");
                    bfw.write(" getExpireTimeHashTableSize=" + resizeDb.getExpireTimeHashTableSize() + "\n");
                    break;
                case SELECT_DB:
                    bfw.write("----------------------------\n");
                    bfw.write(" EntryType=" + e.getType() + "\n");
                    SelectDb selectDb = (SelectDb)e;
                    bfw.write(" SELECT_DB id=" + selectDb.getId() + "\n");
                    break;
                case KEY_VALUE_PAIR:
                    processKV((KeyValuePair)e);
                    break;
                default:
                    bfw.write("----------------------------\n");
                    bfw.write("default EntryType=" + e.getType() + "\n");
                    break;
            }
        }

        bfw.flush();
        bfw.close();
    }

    private void processKV(KeyValuePair kv) throws IOException {
        bfw.write("----------------------------\n");

        EntryType type = kv.getType();
        bfw.write("EntryType=" + type + "\n");

        Long expireTime = kv.getExpireTime();
        bfw.write("expireTime=" + expireTime + "\n");

        ValueType valueType = kv.getValueType();
        bfw.write("valueType=" + valueType + "\n");

        String key = new String(kv.getKey());
        bfw.write("key=" + key + "\n");

        for (byte[] valueBytes : kv.getValues()) {
            String value = new String(valueBytes);
            bfw.write("value=" + value + "\n");
        }

    }
}
