package gov.ismonnet.swing;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import gov.ismonnet.bootstrap.Bootstrap;
import gov.ismonnet.lifecycle.EagerInit;
import gov.ismonnet.lifecycle.LifeCycleService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;

@Module
public abstract class SwingModule {

    @Provides @Singleton
    static SwingWindow swingWindow(SwingWindowFactory factory) {
        // Workaround for https://github.com/google/dagger/issues/890
        return factory.create();
    }

    @Provides @Singleton @BackgroundColor
    static Color backgroundColor() {
        return new Color(33, 33, 33);
    }

    @Binds @IntoSet
    abstract EagerInit eagerInit(EagerInitImpl eagerInitImpl);

    static class EagerInitImpl implements EagerInit {
        @Inject EagerInitImpl(SwingWindow frame) {}
    }
}
