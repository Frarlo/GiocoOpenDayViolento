package gov.ismonnet.resource;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import gov.ismonnet.game.GameSession;

import javax.inject.Singleton;
import java.io.File;

@Module
public abstract class ResourceModule {

    @Binds @Singleton
    abstract ResourceService resourceService(InJarResourceService inJarResourceService);

    @Provides @Singleton @Ripped
    static ResourceService rippedResourceService(ResourceService inJarResourceService, FileResourceService fileResourceService) {
        if(new File("ripped").exists())
            return fileResourceService;
        return inJarResourceService;
    }
}
