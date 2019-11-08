package gov.ismonnet.client;

import dagger.BindsInstance;
import dagger.Component;
import gov.ismonnet.game.renderer.RenderService;
import gov.ismonnet.lifecycle.EagerInit;
import gov.ismonnet.lifecycle.LifeCycleService;

import javax.inject.Named;
import javax.inject.Singleton;
import java.net.InetSocketAddress;
import java.util.Set;

@Singleton
@Component(modules = ClientModule.class)
public interface ClientComponent {

    @ClientPrivate Set<EagerInit> eagerInit();

    LifeCycleService lifeCycle();

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder injectAddress(@Named("socket_address") InetSocketAddress address);

        @BindsInstance
        Builder injectSide(@ClientPrivate RenderService.Side side);

        ClientComponent build();
    }
}
