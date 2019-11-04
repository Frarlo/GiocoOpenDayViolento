package gov.ismonnet.client.rink;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class RinkModule {

    @Binds abstract Rink rink(RinkImpl impl);
}
