package gov.ismonnet.server;

import dagger.BindsInstance;
import dagger.Component;
import gov.ismonnet.game.renderer.RenderService;
import gov.ismonnet.lifecycle.EagerInit;
import gov.ismonnet.lifecycle.LifeCycleService;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Set;

@Singleton
@Component(modules = ServerModule.class)
public interface ServerComponent {

    @ServerPrivate Set<EagerInit> eagerInit();

    LifeCycleService lifeCycle();

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder injectPort(@Named("socket_port") int port);

        @BindsInstance
        Builder injectSide(@ServerPrivate RenderService.Side side);

        ServerComponent build();
    }
}
