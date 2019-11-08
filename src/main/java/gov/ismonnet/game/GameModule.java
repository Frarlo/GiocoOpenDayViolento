package gov.ismonnet.game;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoSet;
import gov.ismonnet.game.physics.PhysicsModule;
import gov.ismonnet.game.physics.PhysicsService;
import gov.ismonnet.game.renderer.RenderService;
import gov.ismonnet.game.renderer.swing.SwingRendererModule;
import gov.ismonnet.game.resource.ResourceModule;
import gov.ismonnet.game.physics.table.TableModule;
import gov.ismonnet.lifecycle.EagerInit;
import gov.ismonnet.netty.core.NetService;

import javax.inject.Inject;

@Module(includes = {
        ResourceModule.class,
        PhysicsModule.class,
        SwingRendererModule.class,
        TableModule.class })
abstract class GameModule {

    @Binds @IntoSet
    abstract EagerInit eagerInit(EagerInitImpl eagerInitImpl);

    static class EagerInitImpl implements EagerInit {
        @Inject EagerInitImpl(NetService netService,
                              PhysicsService physicsService,
                              RenderService renderService) {}
    }
}
