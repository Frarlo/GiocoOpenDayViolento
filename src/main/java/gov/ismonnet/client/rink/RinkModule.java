package gov.ismonnet.client.rink;

import dagger.Binds;
import dagger.Module;

import javax.inject.Singleton;

@Module
public abstract class RinkModule {

    @Binds @Singleton abstract Rink rink(RinkImpl impl);
}
