package gov.ismonnet.swing;

import gov.ismonnet.lifecycle.LifeCycle;
import gov.ismonnet.lifecycle.LifeCycleService;
import gov.ismonnet.util.SneakyThrow;

import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class SwingWindow implements LifeCycle {

    private final LifeCycleService lifeCycleService;

    private final JFrame frame;
    private final AtomicBoolean disposing = new AtomicBoolean(false);

    @Inject SwingWindow(@Named("bootstrap") LifeCycleService lifeCycleService) {
        this.lifeCycleService = lifeCycleService;

        this.frame = new ActualFrame();
        lifeCycleService.beforeStop(() -> frame.setVisible(false));
        lifeCycleService.register(this);
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
        disposing.set(true);
        frame.dispose();
    }

    public void setScreen(JPanel panel) {
        frame.removeAll();
        frame.add(panel);
        frame.setContentPane(panel);
        frame.validate();
    }

    private final class ActualFrame extends JFrame {

        ActualFrame() {
            setTitle("SwingGame");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            setSize(800, 900);
            setResizable(true);
            setLocationRelativeTo(null);
        }

        @Override
        public void dispose() {
            if(!disposing.getAndSet(true))
                SneakyThrow.runUnchecked(lifeCycleService::stop);
            super.dispose();
            disposing.set(false);
        }
    }
}
