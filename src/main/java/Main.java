import gov.ismonnet.bootstrap.swing.DaggerSwingBootstrapComponent;

public class Main {
    public static void main(String[] args) throws Exception {
        DaggerSwingBootstrapComponent.create()
                .bootstrap()
                .start();
    }
}
