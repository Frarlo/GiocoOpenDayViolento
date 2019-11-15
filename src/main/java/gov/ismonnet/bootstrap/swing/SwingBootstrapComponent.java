package gov.ismonnet.bootstrap.swing;

import dagger.Component;
import gov.ismonnet.bootstrap.Bootstrap;

import javax.inject.Singleton;

@Singleton
@Component(modules = SwingBootstrapModule.class)
public interface SwingBootstrapComponent {
    Bootstrap bootstrap();
}
