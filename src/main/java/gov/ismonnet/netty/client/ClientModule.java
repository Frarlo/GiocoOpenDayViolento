package gov.ismonnet.netty.client;

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
import gov.ismonnet.netty.packets.KickPacket;
import gov.ismonnet.netty.packets.PongPacket;

@Module(includes = SharedNetModule.class)
abstract class ClientModule {

    @Provides @NetSession
    static LifeCycleService lifeCycleService() {
        return new LifeCycleManager("client");
    }

    @Binds @NetSession
    abstract NetService netService(ClientNetService clientNetService);

    @Provides @IntoMap @ClassKey(PongPacket.class)
    static PacketParser pingParser() {
        return PongPacket.PARSER;
    }

    @Provides @IntoMap @ClassKey(KickPacket.class)
    static PacketParser disconnectParser() {
        return KickPacket.PARSER;
    }
}
