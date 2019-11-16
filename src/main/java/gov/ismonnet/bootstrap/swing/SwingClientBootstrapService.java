package gov.ismonnet.bootstrap.swing;

import gov.ismonnet.bootstrap.ClientBootstrapService;
import gov.ismonnet.bootstrap.DefaultPort;
import gov.ismonnet.swing.BackgroundColor;
import gov.ismonnet.swing.SwingWindow;
import gov.ismonnet.util.SneakyThrow;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.net.InetSocketAddress;

class SwingClientBootstrapService extends SwingLoadingScreen implements ClientBootstrapService {

    private final int port;
    private final SwingWindow window;

    @Inject SwingClientBootstrapService(@DefaultPort int port,
                                        SwingWindow window,
                                        @BackgroundColor Color backgroundColor,
                                        ImageIcon luca) {
        super(backgroundColor, luca, "Connettendosi all'altro giocatore...");

        this.port = port;
        this.window = window;
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
    public void startWaiting() {
        window.setScreen(this);
    }

    @Override
    public void stopWaiting() {
    }
}
