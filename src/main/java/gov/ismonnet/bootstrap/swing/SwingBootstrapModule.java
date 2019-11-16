package gov.ismonnet.bootstrap.swing;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import gov.ismonnet.bootstrap.BootstrapModule;
import gov.ismonnet.bootstrap.BootstrapService;
import gov.ismonnet.bootstrap.ClientBootstrapService;
import gov.ismonnet.bootstrap.ServerBootstrapService;
import gov.ismonnet.bootstrap.cli.CliBootstrapService;
import gov.ismonnet.bootstrap.cli.StdIn;
import gov.ismonnet.bootstrap.cli.StdOut;
import gov.ismonnet.swing.SwingModule;

import javax.inject.Singleton;
import java.io.InputStream;
import java.io.PrintStream;

@Module(includes = { BootstrapModule.class, SwingModule.class, SwingLoadingScreenModule.class })
abstract class SwingBootstrapModule {

    @Binds @Singleton
    abstract BootstrapService bootstrapService(CliBootstrapService cliBootstrapService);

    @Binds @Singleton
    abstract ServerBootstrapService serverBootstrapService(SwingServerBootstrapService swingServerBootstrapService);

    @Binds @Singleton
    abstract ClientBootstrapService clientBootstrapService(SwingClientBootstrapService cliClientBootstrapService);

    @Provides @StdOut
    static PrintStream out() {
        return System.out;
    }

    @Provides @StdIn
    static InputStream in() {
        return System.in;
    }
}
