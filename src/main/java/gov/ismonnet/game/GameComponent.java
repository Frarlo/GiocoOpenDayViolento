package gov.ismonnet.game;

import dagger.BindsInstance;
import dagger.Subcomponent;
import gov.ismonnet.game.renderer.RenderService;
import gov.ismonnet.lifecycle.EagerInit;

import java.util.Set;

@GameSession
@Subcomponent(modules = GameModule.class)
public interface GameComponent {

    Set<EagerInit> eagerInit();

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        Builder injectSide(RenderService.Side side);

        GameComponent build();
    }
}
