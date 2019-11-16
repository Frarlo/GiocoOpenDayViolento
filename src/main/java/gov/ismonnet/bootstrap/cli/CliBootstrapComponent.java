package gov.ismonnet.bootstrap.cli;

import dagger.Component;
import gov.ismonnet.bootstrap.Bootstrapper;

import javax.inject.Singleton;

@Singleton
@Component(modules = CliBootstrapModule.class)
public interface CliBootstrapComponent {
    Bootstrapper bootstrap();
}
