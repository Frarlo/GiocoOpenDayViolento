package gov.ismonnet.game;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoSet;
import gov.ismonnet.game.physics.PhysicsModule;
import gov.ismonnet.game.physics.PhysicsService;
import gov.ismonnet.game.physics.table.TableModule;
import gov.ismonnet.game.renderer.RenderService;
import gov.ismonnet.game.renderer.swing.SwingRendererModule;
import gov.ismonnet.resource.ResourceModule;
import gov.ismonnet.lifecycle.EagerInit;
import gov.ismonnet.lifecycle.LifeCycleManager;
import gov.ismonnet.lifecycle.LifeCycleService;

import javax.inject.Inject;

@Module(includes = {
        PhysicsModule.class,
        SwingRendererModule.class,
        TableModule.class })
abstract class GameModule {

    @Binds @GameSession
    abstract LifeCycleService lifeCycleService(LifeCycleManager lifeCycleManager);

    @Binds @IntoSet
    abstract EagerInit eagerInit(EagerInitImpl eagerInitImpl);

    static class EagerInitImpl implements EagerInit {
        @Inject EagerInitImpl(PhysicsService physicsService,
                              RenderService renderService) {}
    }
}
