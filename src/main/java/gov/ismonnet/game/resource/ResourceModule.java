package gov.ismonnet.game.resource;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import gov.ismonnet.game.GameSession;

import java.io.File;

@Module
public abstract class ResourceModule {

    @Binds @GameSession
    abstract ResourceService resourceService(InJarResourceService inJarResourceService);

    @Provides @GameSession @Ripped
    static ResourceService rippedResourceService(ResourceService inJarResourceService, FileResourceService fileResourceService) {
        if(new File("ripped").exists())
            return fileResourceService;
        return inJarResourceService;
    }
}
