import gov.ismonnet.client.ClientComponent;
import gov.ismonnet.client.DaggerClientComponent;
import gov.ismonnet.game.renderer.RenderService;
import gov.ismonnet.lifecycle.LifeCycleService;
import gov.ismonnet.server.DaggerServerComponent;
import gov.ismonnet.server.ServerComponent;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        final Scanner sc = new Scanner(System.in);
        System.out.println("Select 1. server or 2. client");

        int n = -1;
        boolean first = true;
        do {
            if(!first)
                System.out.println("Invalid selection");
            first = false;

            try {
                n = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException ex) {
                // Ignored
            }
        } while (n != 1 && n != 2);

        final LifeCycleService lifeCycle;
        if(n == 1) {
            ServerComponent serverComponent = DaggerServerComponent.builder()
                    .injectSide(RenderService.Side.LEFT)
                    .injectPort(3121)
                    .build();
            serverComponent.eagerInit();
            lifeCycle = serverComponent.lifeCycle();
        } else {
            ClientComponent clientComponent = DaggerClientComponent.builder()
                    .injectSide(RenderService.Side.RIGHT)
                    .injectAddress(new InetSocketAddress(InetAddress.getLoopbackAddress(), 3121))
                    .build();
            clientComponent.eagerInit();
            lifeCycle = clientComponent.lifeCycle();
        }

        try {
            lifeCycle.start();
        } catch (Throwable t) {
            t.printStackTrace();
            lifeCycle.stop();
        }
    }
}
