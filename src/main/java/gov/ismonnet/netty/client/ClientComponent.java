package gov.ismonnet.netty.client;

import dagger.BindsInstance;
import dagger.Subcomponent;
import gov.ismonnet.lifecycle.LifeCycleService;
import gov.ismonnet.netty.NetSession;
import gov.ismonnet.netty.core.NetService;

import javax.inject.Named;
import java.net.InetSocketAddress;

@NetSession
@Subcomponent(modules = ClientModule.class)
public interface ClientComponent {

    LifeCycleService lifeCycle();

    NetService netService();

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        Builder injectAddress(@Named("socket_address") InetSocketAddress address);

        ClientComponent build();
    }
}
