package gov.ismonnet.bootstrap.swing;

import gov.ismonnet.bootstrap.ClientBootstrapService;
import gov.ismonnet.bootstrap.DefaultPort;
import gov.ismonnet.bootstrap.UndecidedException;
import gov.ismonnet.lifecycle.LifeCycleService;
import gov.ismonnet.swing.BackgroundColor;
import gov.ismonnet.swing.SwingWindow;
import gov.ismonnet.util.SneakyThrow;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

class SwingClientBootstrapService extends SwingLoadingScreen implements ClientBootstrapService {

    private final int port;
    private final SwingWindow window;

    private AtomicReference<LifeCycleService> lifeCycleService;

    @Inject SwingClientBootstrapService(@DefaultPort int port,
                                        SwingWindow window,
                                        @BackgroundColor Color backgroundColor,
                                        ImageIcon luca) {
        super(backgroundColor, luca);

        this.port = port;
        this.window = window;
        this.lifeCycleService = new AtomicReference<>();
    }

    @Override
    public void cancel() {
        // Do it in another thread to not stop the loading screen
        final LifeCycleService lifeCycleService = this.lifeCycleService.getAndSet(null);
        if(lifeCycleService != null) {
            message = "Tornando al menu principale...";
            Executors.newSingleThreadExecutor()
                    .execute(() -> SneakyThrow.runUnchecked(lifeCycleService::stop));
        }
    }

    @Override
    public InetSocketAddress chooseAddress() throws UndecidedException {
        final String address = SneakyThrow.callUnchecked(() ->
                AddressDialog.getAddress(SwingUtilities.getWindowAncestor(this)).get());
        if(address == null)
            throw new UndecidedException();
        return new InetSocketAddress(address, port);
    }

    @Override
    public void startWaiting(LifeCycleService lifeCycleService) {
        message = "Connettendosi all'altro giocatore...";

        this.lifeCycleService.set(lifeCycleService);
        window.setScreen(this);
    }

    @Override
    public void stopWaiting() {
        lifeCycleService.set(null);
    }
}
