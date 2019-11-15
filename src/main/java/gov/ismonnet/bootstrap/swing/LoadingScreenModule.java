package gov.ismonnet.bootstrap.swing;

import dagger.Module;
import dagger.Provides;
import gov.ismonnet.resource.ResourceService;

import javax.inject.Singleton;
import javax.swing.*;
import java.awt.*;

@Module
public class LoadingScreenModule {

    @Provides @Singleton
    static ImageIcon luca(ResourceService resourceService) {
        return new ImageIcon(resourceService.getResourceUrl("luca.gif"));
    }
}
