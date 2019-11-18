package gov.ismonnet.game.renderer.swing;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import gov.ismonnet.game.GameSession;
import gov.ismonnet.game.renderer.RenderService;
import gov.ismonnet.game.renderer.swing.renderer.SwingRendererModule;
import gov.ismonnet.game.renderer.swing.screen.SwingScreenModule;

@Module(includes = { SwingRendererModule.class, SwingScreenModule.class })
public abstract class SwingRenderModule {

    @Provides @GameSession
    static SwingRenderService renderService0(SwingRenderServiceFactory factory) {
        // Workaround for https://github.com/google/dagger/issues/890
        return factory.create();
    }

    @Binds @GameSession
    abstract RenderService renderService(SwingRenderService swingRenderService);
}
