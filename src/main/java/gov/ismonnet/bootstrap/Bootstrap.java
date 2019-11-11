package gov.ismonnet.bootstrap;

import gov.ismonnet.game.GameComponent;
import gov.ismonnet.game.renderer.RenderService;
import gov.ismonnet.lifecycle.LifeCycleService;
import gov.ismonnet.netty.client.ClientComponent;
import gov.ismonnet.netty.core.NetService;
import gov.ismonnet.netty.server.ServerComponent;
import gov.ismonnet.util.SneakyThrow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class Bootstrap {

    private static final Logger LOGGER = LogManager.getLogger(Bootstrap.class);

    private final BootstrapService bootstrapService;

    private final ServerComponent.Builder serverBuilder;
    private final ClientComponent.Builder clientBuilder;
    private final GameComponent.Builder gameBuilder;

    @Inject Bootstrap(BootstrapService bootstrapService,
                      ServerComponent.Builder serverBuilder,
                      ClientComponent.Builder clientBuilder,
                      GameComponent.Builder gameBuilder) {
        this.bootstrapService = bootstrapService;

        this.serverBuilder = serverBuilder;
        this.clientBuilder = clientBuilder;
        this.gameBuilder = gameBuilder;
    }

    public void start() throws Exception {
        final RenderService.Side side;
        final boolean spawnPuck;
        final NetService netService;
        final LifeCycleService netLifeCycle;

        switch (bootstrapService.chooseNetSide()) {
            case SERVER:
                final ServerComponent serverComponent = serverBuilder
                        .injectPort(bootstrapService.choosePort())
                        .build();
                side = RenderService.Side.LEFT;
                spawnPuck = true;
                netService = serverComponent.netService();
                netLifeCycle = serverComponent.lifeCycle();
                break;
            case CLIENT:
                final ClientComponent clientComponent = clientBuilder
                        .injectAddress(bootstrapService.chooseAddress())
                        .build();
                side = RenderService.Side.RIGHT;
                spawnPuck = false;
                netService = clientComponent.netService();
                netLifeCycle = clientComponent.lifeCycle();
                break;
            default:
                throw new AssertionError("Bootstrapper hans't implemented all possible user choices");
        }

        try {
            netLifeCycle.start();
        } catch (Throwable t) {
            LOGGER.fatal("Exception while starting net lifecycle", t);

            netLifeCycle.stop();
            return;
        }

        final GameComponent gameComponent = gameBuilder
                .injectNetService(netService)
                .injectSide(side)
                .injectSpawnPuck(spawnPuck)
                .build();
        gameComponent.eagerInit();

        try {
            gameComponent.lifeCycle().start();
        } catch (Throwable t) {
            LOGGER.fatal("Exception while starting game lifecycle", t);

            gameComponent.lifeCycle().stop();
            netLifeCycle.stop();
            return;
        }

        // Register both lifecycle to depend on the other one
        // so if one gets stopped the other does too
        final AtomicBoolean stopping = new AtomicBoolean(false);
        final Consumer<LifeCycleService> onStop = (other) -> {
            if(!stopping.getAndSet(true))
                SneakyThrow.runUnchecked(other::stop);
        };

        netLifeCycle.beforeStop(() -> onStop.accept(gameComponent.lifeCycle()));
        gameComponent.lifeCycle().afterStop(() -> onStop.accept(netLifeCycle));
    }
}
