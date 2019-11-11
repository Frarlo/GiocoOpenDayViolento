package gov.ismonnet.game;

import dagger.BindsInstance;
import dagger.Subcomponent;
import gov.ismonnet.game.renderer.RenderService;
import gov.ismonnet.lifecycle.EagerInit;
import gov.ismonnet.lifecycle.LifeCycleService;
import gov.ismonnet.netty.core.NetService;

import javax.inject.Named;
import java.util.Set;

@GameSession
@Subcomponent(modules = GameModule.class)
public interface GameComponent {

    Set<EagerInit> eagerInit();

    LifeCycleService lifeCycle();

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        Builder injectSide(RenderService.Side side);

        @BindsInstance
        Builder injectSpawnPuck(@Named("spawn_puck") boolean spawnPuck);

        @BindsInstance
        Builder injectNetService(NetService netService);

        GameComponent build();
    }
}
