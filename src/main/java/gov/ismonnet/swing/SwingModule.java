package gov.ismonnet.swing;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoSet;
import gov.ismonnet.lifecycle.EagerInit;

import javax.inject.Inject;

@Module
public abstract class SwingModule {

    @Binds @IntoSet
    abstract EagerInit eagerInit(EagerInitImpl eagerInitImpl);

    static class EagerInitImpl implements EagerInit {
        @Inject EagerInitImpl(SwingWindow frame) {}
    }
}
