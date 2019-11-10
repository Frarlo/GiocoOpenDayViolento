package gov.ismonnet.bootstrap.cli;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import gov.ismonnet.bootstrap.BootstrapModule;
import gov.ismonnet.bootstrap.BootstrapService;

import javax.inject.Singleton;
import java.io.InputStream;
import java.io.PrintStream;

@Module(includes = { BootstrapModule.class })
abstract class CliBootstrapModule {

    @Binds @Singleton
    abstract BootstrapService bootstrapService(CliBootstrapService cliBootstrapService);

    @Provides static PrintStream out() {
        return System.out;
    }

    @Provides static InputStream in() {
        return System.in;
    }
}
