package gov.ismonnet.client.resource;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;
import java.io.File;

@Module
public abstract class ResourceModule {

    @Binds @Singleton
    abstract ResourceService resourceService(InJarResourceService inJarResourceService);

    @Provides @Singleton @Ripped
    static ResourceService rippedResourceService(ResourceService inJarResourceService, FileResourceService fileResourceService) {
        System.out.println(new File("ripped").getAbsoluteFile());
        if(new File("ripped").exists())
            return fileResourceService;
        return inJarResourceService;
    }
}
