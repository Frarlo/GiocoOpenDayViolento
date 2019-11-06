package gov.ismonnet.client;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoSet;
import gov.ismonnet.client.renderer.RenderService;
import gov.ismonnet.lifecycle.EagerSingleton;
import gov.ismonnet.lifecycle.LifeCycleManager;
import gov.ismonnet.lifecycle.LifeCycleService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Module
abstract class ClientModule {

    @Binds @Singleton abstract Client client(ClientImpl impl);

    @Binds @Singleton abstract LifeCycleService lifeCycle(LifeCycleManager lifeCycleManager);

    @Binds @IntoSet
    abstract EagerSingleton eagerSingleton(EagerSingletons eagerSingletons);

    static class EagerSingletons implements EagerSingleton {
        @Inject EagerSingletons(Client client, RenderService renderService) {}
    }
}
