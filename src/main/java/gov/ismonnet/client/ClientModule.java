package gov.ismonnet.client;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ClientModule {

    @Binds abstract Client client(ClientImpl impl);
}
