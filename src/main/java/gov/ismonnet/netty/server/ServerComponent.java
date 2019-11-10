package gov.ismonnet.netty.server;

import dagger.BindsInstance;
import dagger.Subcomponent;
import gov.ismonnet.lifecycle.LifeCycleService;
import gov.ismonnet.netty.NetSession;
import gov.ismonnet.netty.core.NetService;

import javax.inject.Named;

@NetSession
@Subcomponent(modules = ServerModule.class)
public interface ServerComponent {

    LifeCycleService lifeCycle();

    NetService netService();

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        Builder injectPort(@Named("socket_port") int port);

        ServerComponent build();
    }
}
