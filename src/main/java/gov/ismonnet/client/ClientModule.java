package gov.ismonnet.client;

import dagger.Binds;
import dagger.Module;

import javax.inject.Singleton;

@Module
public abstract class ClientModule {

    @Binds @Singleton abstract Client client(ClientImpl impl);
}
