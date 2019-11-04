import gov.ismonnet.client.DaggerGameComponent;

public class Main {

    public static void main(String[] args) {
        DaggerGameComponent.create().getClient();
    }
}
