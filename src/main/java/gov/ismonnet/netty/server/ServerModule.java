package gov.ismonnet.netty.server;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import gov.ismonnet.lifecycle.LifeCycleManager;
import gov.ismonnet.lifecycle.LifeCycleService;
import gov.ismonnet.netty.NetSession;
import gov.ismonnet.netty.SharedNetModule;
import gov.ismonnet.netty.core.NetService;
import gov.ismonnet.netty.core.PacketParser;
import gov.ismonnet.netty.packets.DisconnectPacket;
import gov.ismonnet.netty.packets.PingPacket;

@Module(includes = SharedNetModule.class)
public abstract class ServerModule {

    @Binds @NetSession
    abstract LifeCycleService lifeCycleService(LifeCycleManager lifeCycleManager);

    @Binds @NetSession
    abstract NetService netService(ServerNetService serverNetService);

    @Provides @IntoMap @ClassKey(PingPacket.class)
    static PacketParser pingParser() {
        return PingPacket.PARSER;
    }

    @Provides @IntoMap @ClassKey(DisconnectPacket.class)
    static PacketParser disconnectParser() {
        return DisconnectPacket.PARSER;
    }
}
