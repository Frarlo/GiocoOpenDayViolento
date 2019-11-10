package gov.ismonnet.netty;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import gov.ismonnet.netty.core.Packet;
import gov.ismonnet.netty.core.PacketIdService;
import gov.ismonnet.netty.packets.DisconnectPacket;
import gov.ismonnet.netty.packets.KickPacket;
import gov.ismonnet.netty.packets.PingPacket;
import gov.ismonnet.netty.packets.PongPacket;

import javax.inject.Named;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Module
public abstract class SharedNetModule {

    @Binds @NetSession
    abstract PacketIdService packetIdService(PacketIdServiceImpl impl);

    @Provides @Named("keep_alive_timeout")
    static int keepAliveTimeout() {
        return 10 * 1000;
    }

    @Provides @NetSession
    static Map<Class<? extends Packet>, Byte> packetIds() {
        final Map<Class<? extends Packet>, Byte> temp = new HashMap<>();

        // The packets with the same id are
        // always a Server-Client pair
        // So they can have the same id

        temp.put(PingPacket.class, (byte) 0);
        temp.put(PongPacket.class, (byte) 0);

        temp.put(DisconnectPacket.class, (byte) 1);
        temp.put(KickPacket.class, (byte) 1);

        return Collections.unmodifiableMap(temp);
    }
}
