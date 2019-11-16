package gov.ismonnet.bootstrap;

import dagger.Module;
import dagger.Provides;
import gov.ismonnet.game.GameComponent;
import gov.ismonnet.lifecycle.LifeCycleManager;
import gov.ismonnet.lifecycle.LifeCycleService;
import gov.ismonnet.netty.client.ClientComponent;
import gov.ismonnet.netty.server.ServerComponent;
import gov.ismonnet.resource.ResourceModule;

import javax.inject.Singleton;

@Module(includes = ResourceModule.class,
        subcomponents = {
                ServerComponent.class,
                ClientComponent.class,
                GameComponent.class
        })
public abstract class BootstrapModule {

    @Provides @Singleton @Bootstrap
    static LifeCycleService lifeCycleService() {
        return new LifeCycleManager("bootstrap");
    }

    @Provides @DefaultPort
    static int defaultPort() {
        return 3121;
    }
}
