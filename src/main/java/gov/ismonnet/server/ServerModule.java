package gov.ismonnet.server;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ClassKey;
import dagger.multibindings.ElementsIntoSet;
import dagger.multibindings.IntoMap;
import gov.ismonnet.game.GameComponent;
import gov.ismonnet.game.renderer.RenderService;
import gov.ismonnet.lifecycle.EagerInit;
import gov.ismonnet.lifecycle.LifeCycleManager;
import gov.ismonnet.lifecycle.LifeCycleService;
import gov.ismonnet.netty.SharedNetModule;
import gov.ismonnet.netty.core.NetService;
import gov.ismonnet.netty.core.PacketParser;
import gov.ismonnet.netty.packets.DisconnectPacket;
import gov.ismonnet.netty.packets.PingPacket;

import javax.inject.Singleton;
import java.util.Set;

@Module(includes = SharedNetModule.class, subcomponents = GameComponent.class)
public abstract class ServerModule {

    @Provides @Singleton
    static GameComponent gameComponent(GameComponent.Builder gameBuilder, @ServerPrivate RenderService.Side side) {
        return gameBuilder.injectSide(side).build();
    }

    @Provides @ElementsIntoSet @ServerPrivate
    static Set<EagerInit> gameEagerInit(GameComponent gameComponent) {
        return gameComponent.eagerInit();
    }

    @Binds @Singleton
    abstract LifeCycleService lifeCycleService(LifeCycleManager lifeCycleManager);

    @Binds @Singleton
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
