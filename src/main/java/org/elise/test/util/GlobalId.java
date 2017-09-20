package org.elise.test.util;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * Created by Glenn on  2017/9/18 0018 18:17.
 */


public class GlobalId {

    public static String generate16(){
        UUID uuid = UUID.randomUUID();
        Long MostBits  = uuid.getMostSignificantBits();
        Long leastBits = uuid.getLeastSignificantBits();
        StringBuilder sb = new StringBuilder();
        sb.append(Long.toHexString(leastBits));
        sb.append(Long.toHexString(MostBits));
        return sb.toString();
    }
    public static String generate32(){
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        Long leastBits1 = uuid1.getLeastSignificantBits();
        Long MostBits1  = uuid1.getMostSignificantBits();
        Long leastBits2 = uuid2.getLeastSignificantBits();
        Long MostBits2  = uuid2.getMostSignificantBits();
        StringBuilder sb = new StringBuilder();
        sb.append(Long.toHexString(leastBits1));
        sb.append(Long.toHexString(leastBits2));
        sb.append(Long.toHexString(MostBits1));
        sb.append(Long.toHexString(MostBits2));
        return sb.toString();
    }

    public static ByteBuffer generate16Bin(){
        ByteBuffer buffer = ByteBuffer.allocate(16);
        UUID uuid = UUID.randomUUID();
        buffer.putLong(uuid.getLeastSignificantBits());
        buffer.putLong(uuid.getMostSignificantBits());
        return buffer;
    }
    public static ByteBuffer generate32Bin(){
        ByteBuffer buffer = ByteBuffer.allocate(32);
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        buffer.putLong(uuid1.getLeastSignificantBits());
        buffer.putLong(uuid2.getLeastSignificantBits());
        buffer.putLong(uuid1.getMostSignificantBits());
        buffer.putLong(uuid2.getMostSignificantBits());
        return buffer;
    }
}
