package gov.ismonnet.client;

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
import gov.ismonnet.netty.packets.KickPacket;
import gov.ismonnet.netty.packets.PongPacket;

import javax.inject.Singleton;
import java.util.Set;

@Module(includes = SharedNetModule.class, subcomponents = GameComponent.class)
abstract class ClientModule {

    @Provides @Singleton
    static GameComponent game(GameComponent.Builder gameBuilder, @ClientPrivate RenderService.Side side) {
        return gameBuilder.injectSide(side).build();
    }

    @Provides @ElementsIntoSet @ClientPrivate
    static Set<EagerInit> gameEagerInit(GameComponent gameComponent) {
        return gameComponent.eagerInit();
    }

    @Binds @Singleton
    abstract LifeCycleService lifeCycleService(LifeCycleManager lifeCycleManager);

    @Binds @Singleton
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
