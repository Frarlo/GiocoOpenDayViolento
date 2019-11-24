package gov.ismonnet.bootstrap.cli;

import gov.ismonnet.bootstrap.BootstrapService;

import javax.inject.Inject;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

class CliBootstrapService implements BootstrapService {

    private final PrintStream out;

    private final Scanner scanner;
    private final InputStream in;

    @Inject CliBootstrapService(@StdOut PrintStream out,
                                @StdIn InputStream in) {
        this.in = in;
        this.scanner = new Scanner(in);
        this.out = out;
    }

    @Override
    public NetSide chooseNetSide() {
        out.println("Select action: ");
        out.println("0. Exit");
        out.println("1. Host server");
        out.println("2. Connect to server");

        switch (readInt(0, 2)) {
            case 1:
                return NetSide.SERVER;
            case 2:
                return NetSide.CLIENT;
            default:
                System.exit(0);
                return null;
        }
    }

    private int readInt(int min, int max) {
        int n = -1;
        boolean first = true;
        do {
            if(!first)
                out.printf("Invalid number [min: %s, max: %s]. Retry: ", min, max);
            first = false;

            try {
                n = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException ex) {
                // Ignored
            }
        } while (n < min || n > max);

        return n;
    }
}
