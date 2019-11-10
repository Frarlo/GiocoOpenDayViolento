import gov.ismonnet.bootstrap.cli.DaggerCliBootstrapComponent;

public class Main {
    public static void main(String[] args) throws Exception {
        DaggerCliBootstrapComponent.create()
                .bootstrap()
                .start();
    }
}
