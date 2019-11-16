package gov.ismonnet.bootstrap.cli;

import gov.ismonnet.bootstrap.ClientBootstrapService;
import gov.ismonnet.bootstrap.DefaultPort;

import javax.inject.Inject;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

class CliClientBootstrapService implements ClientBootstrapService {

    private final int port;

    private final PrintStream out;

    private final Scanner scanner;
    private final InputStream in;

    @Inject CliClientBootstrapService(@DefaultPort int port,
                                      @StdOut PrintStream out,
                                      @StdIn InputStream in) {
        this.port = port;

        this.in = in;
        this.scanner = new Scanner(in);
        this.out = out;
    }

    @Override
    public InetSocketAddress chooseAddress() {
        out.println("Enter the server address: ");
        return new InetSocketAddress(readAddress(), port);
    }

    private InetAddress readAddress() {
        while (true) {
            try {
                return InetAddress.getByName(scanner.nextLine());
            } catch (UnknownHostException e) {
                out.print("Invalid ipv4. Retry: ");
            }
        }
    }

    @Override
    public void startWaiting() {
        out.println("Connecting to the server...");
    }

    @Override
    public void stopWaiting() {
    }
}
