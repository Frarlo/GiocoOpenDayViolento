package gov.ismonnet.bootstrap.swing;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import gov.ismonnet.bootstrap.BootstrapModule;
import gov.ismonnet.bootstrap.BootstrapService;
import gov.ismonnet.bootstrap.ClientBootstrapService;
import gov.ismonnet.bootstrap.ServerBootstrapService;
import gov.ismonnet.resource.ResourceService;
import gov.ismonnet.swing.SwingModule;

import javax.inject.Singleton;
import java.awt.*;

@Module(includes = { BootstrapModule.class, SwingModule.class, SwingLoadingScreenModule.class })
abstract class SwingBootstrapModule {

    @Binds @Singleton
    abstract BootstrapService bootstrapService(SwingBootstrapService cliBootstrapService);

    @Binds @Singleton
    abstract ServerBootstrapService serverBootstrapService(SwingServerBootstrapService swingServerBootstrapService);

    @Binds @Singleton
    abstract ClientBootstrapService clientBootstrapService(SwingClientBootstrapService cliClientBootstrapService);

    @Provides @Singleton
    static Image backgroundImg(ResourceService resourceService) {
        return resourceService.getImageResources("main_menu.png");
    }
}
