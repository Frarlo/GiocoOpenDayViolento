package gov.ismonnet.bootstrap.swing;

import dagger.Module;
import dagger.Provides;
import gov.ismonnet.resource.ResourceService;

import javax.inject.Singleton;
import javax.swing.*;

@Module
abstract class SwingLoadingScreenModule {

    @Provides @Singleton
    static ImageIcon luca(ResourceService resourceService) {
        return new ImageIcon(resourceService.getResourceUrl("luca.gif"));
    }
}
