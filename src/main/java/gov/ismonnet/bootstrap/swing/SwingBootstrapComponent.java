package gov.ismonnet.bootstrap.swing;

import dagger.Component;
import gov.ismonnet.bootstrap.Bootstrapper;

import javax.inject.Singleton;

@Singleton
@Component(modules = SwingBootstrapModule.class)
public interface SwingBootstrapComponent {
    Bootstrapper bootstrap();
}
