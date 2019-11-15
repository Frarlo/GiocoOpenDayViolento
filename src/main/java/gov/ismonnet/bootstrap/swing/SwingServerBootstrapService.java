package gov.ismonnet.bootstrap.swing;

import gov.ismonnet.bootstrap.ServerBootstrapService;
import gov.ismonnet.swing.SwingWindow;

import javax.inject.Inject;
import javax.swing.*;

public class SwingServerBootstrapService extends SwingLoadingScreen implements ServerBootstrapService {

    private final SwingWindow window;

    @Inject SwingServerBootstrapService(SwingWindow window, ImageIcon luca) {
        super(luca);

        this.window = window;
    }

    @Override
    public int choosePort() {
        return 3121;
    }

    @Override
    public void startWaiting() {
        window.setScreen(this);
    }

    @Override
    public void stopWaiting() {
    }
}
