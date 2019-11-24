package gov.ismonnet.bootstrap.cli;

import gov.ismonnet.bootstrap.DefaultPort;
import gov.ismonnet.bootstrap.ServerBootstrapService;
import gov.ismonnet.lifecycle.LifeCycleService;

import javax.inject.Inject;
import java.io.PrintStream;

public class CliServerBootstrapService implements ServerBootstrapService {

    private final int port;

    private final PrintStream out;

    @Inject CliServerBootstrapService(@DefaultPort int port,
                                      @StdOut PrintStream out) {
        this.port = port;
        this.out = out;
    }

    @Override
    public int choosePort() {
        return port;
    }

    @Override
    public void startWaiting(LifeCycleService lifeCycleService) {
        out.println("Waiting for client to connect...");
    }

    @Override
    public void stopWaiting() {
    }
}
