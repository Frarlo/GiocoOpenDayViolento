package gov.ismonnet.bootstrap.cli;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import gov.ismonnet.bootstrap.BootstrapModule;
import gov.ismonnet.bootstrap.BootstrapService;
import gov.ismonnet.bootstrap.ClientBootstrapService;
import gov.ismonnet.bootstrap.ServerBootstrapService;
import gov.ismonnet.swing.SwingModule;

import javax.inject.Singleton;
import java.io.InputStream;
import java.io.PrintStream;

@Module(includes = { BootstrapModule.class, SwingModule.class })
abstract class CliBootstrapModule {

    @Binds @Singleton
    abstract BootstrapService bootstrapService(CliBootstrapService cliBootstrapService);

    @Binds @Singleton
    abstract ServerBootstrapService serverBootstrapService(CliServerBootstrapService cliServerBootstrapService);

    @Binds @Singleton
    abstract ClientBootstrapService clientBootstrapService(CliClientBootstrapService cliClientBootstrapService);

    @Provides @StdOut
    static PrintStream out() {
        return System.out;
    }

    @Provides @StdIn
    static InputStream in() {
        return System.in;
    }
}
