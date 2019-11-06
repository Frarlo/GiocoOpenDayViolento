package gov.ismonnet.client.resource;

import dagger.Binds;
import dagger.Module;

import javax.inject.Singleton;

@Module
public abstract class ResourceModule {

    @Binds @Singleton
    abstract ResourceService resourceService(InJarResourceService inJarResourceService);
}
