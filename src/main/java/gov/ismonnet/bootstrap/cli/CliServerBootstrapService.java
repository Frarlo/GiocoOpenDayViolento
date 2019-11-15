package gov.ismonnet.bootstrap.cli;

import gov.ismonnet.bootstrap.ServerBootstrapService;

import javax.inject.Inject;
import java.io.PrintStream;

public class CliServerBootstrapService implements ServerBootstrapService {

    private final PrintStream out;

    @Inject CliServerBootstrapService(PrintStream out) {
        this.out = out;
    }

    @Override
    public int choosePort() {
        return CliBootstrapService.PORT;
    }

    @Override
    public void startWaiting() {
        out.println("Waiting for client to connect...");
    }

    @Override
    public void stopWaiting() {
    }
}
