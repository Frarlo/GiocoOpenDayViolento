package gov.ismonnet.bootstrap;

import gov.ismonnet.bootstrap.swing.UndecidedException;
import gov.ismonnet.game.GameComponent;
import gov.ismonnet.game.renderer.RenderService;
import gov.ismonnet.lifecycle.EagerInit;
import gov.ismonnet.lifecycle.LifeCycleService;
import gov.ismonnet.netty.client.ClientComponent;
import gov.ismonnet.netty.core.NetService;
import gov.ismonnet.netty.server.ServerComponent;
import gov.ismonnet.util.SneakyThrow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class Bootstrapper {

    private static final Logger LOGGER = LogManager.getLogger(Bootstrapper.class);

    private final LifeCycleService bootstrapLifeCycle;

    private final BootstrapService bootstrapService;
    private final ServerBootstrapService serverBootstrapService;
    private final ClientBootstrapService clientBootstrapService;

    private final ServerComponent.Builder serverBuilder;
    private final ClientComponent.Builder clientBuilder;
    private final GameComponent.Builder gameBuilder;

    @Inject
    Bootstrapper(@Bootstrap LifeCycleService bootstrapLifeCycle,
                 Set<EagerInit> eagerInit,
                 BootstrapService bootstrapService,
                 ServerBootstrapService serverBootstrapService,
                 ClientBootstrapService clientBootstrapService,
                 ServerComponent.Builder serverBuilder,
                 ClientComponent.Builder clientBuilder,
                 GameComponent.Builder gameBuilder) {

        this.bootstrapLifeCycle = bootstrapLifeCycle;

        this.bootstrapService = bootstrapService;
        this.serverBootstrapService = serverBootstrapService;
        this.clientBootstrapService = clientBootstrapService;

        this.serverBuilder = serverBuilder;
        this.clientBuilder = clientBuilder;
        this.gameBuilder = gameBuilder;
    }

    public void start() throws Exception {

        final Thread currentThread = Thread.currentThread();

        bootstrapLifeCycle.start();
        // When debugging with the program started twice
        // (either both ones in running or both in debugging)
        // when interrupting the main thread in one,
        // IntelliJ seem to somehow crash in the other too and exit with -1
        // In reality it works fine, so debug one and run the other
        bootstrapLifeCycle.beforeStop(currentThread::interrupt);

        try {
            while (!currentThread.isInterrupted()) {
                try {
                    start0();
                } catch (UndecidedException ex) {
                    // Ignored, just restart
                }
            }
        } catch (InterruptedException ex) {
            // Ignored
        } catch (Throwable t) {
            LOGGER.fatal("Unchaught exception", t);
            System.exit(-1);
        }
    }

    private void start0() throws Exception {

        final RenderService.Side side;
        final boolean spawnPuck;
        final NetService netService;
        final LifeCycleService netLifeCycle;

        switch (bootstrapService.chooseNetSide()) {
            case SERVER:
                final ServerComponent serverComponent = serverBuilder
                        .injectPort(serverBootstrapService.choosePort())
                        .build();
                side = RenderService.Side.LEFT;
                spawnPuck = true;
                netService = serverComponent.netService();
                netLifeCycle = serverComponent.lifeCycle();

                serverBootstrapService.startWaiting();
                netLifeCycle.beforeStop(serverBootstrapService::stopWaiting);

                break;
            case CLIENT:
                final ClientComponent clientComponent = clientBuilder
                        .injectAddress(clientBootstrapService.chooseAddress())
                        .build();
                side = RenderService.Side.RIGHT;
                spawnPuck = false;
                netService = clientComponent.netService();
                netLifeCycle = clientComponent.lifeCycle();

                clientBootstrapService.startWaiting();
                netLifeCycle.afterStop(clientBootstrapService::stopWaiting);

                break;
            default:
                throw new AssertionError("Bootstrapper hasn't implemented all possible user choices");
        }

        final AtomicReference<LifeCycleService> netLifeCycleReference = new AtomicReference<>(netLifeCycle);
        bootstrapLifeCycle.beforeStop(() -> {
            if(netLifeCycleReference.get() != null)
                SneakyThrow.runUnchecked(netLifeCycleReference.get()::stop);
        });

        try {
            netLifeCycle.start();
        } catch (InterruptedException ex) {
            throw ex;
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

        final LifeCycleService mergedGameLifeCycle = netLifeCycle.merge(gameComponent.lifeCycle());
        mergedGameLifeCycle.afterStop(() -> netLifeCycleReference.set(null));

        try {
            gameComponent.lifeCycle().start();
        } catch (InterruptedException ex) {
            netLifeCycleReference.set(null);
            throw ex;
        } catch (Throwable t) {
            LOGGER.fatal("Exception while starting game lifecycle", t);

            gameComponent.lifeCycle().stop();
            netLifeCycle.stop();
            netLifeCycleReference.set(null);
            return;
        }

        final CountDownLatch latch = new CountDownLatch(1);
        mergedGameLifeCycle.afterStop(latch::countDown);
        latch.await();
    }
}
