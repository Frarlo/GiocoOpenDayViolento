import gov.ismonnet.client.DaggerGameComponent;
import gov.ismonnet.client.GameComponent;
import gov.ismonnet.client.table.Table;

public class Main {

    public static void main(String[] args) {
        GameComponent game = DaggerGameComponent.builder()
                .injectSide(Table.Side.LEFT)
                .build();
        game.eagerSingletons();
        game.lifeCycle().start();
    }
}
