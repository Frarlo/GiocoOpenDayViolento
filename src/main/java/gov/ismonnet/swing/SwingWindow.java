package gov.ismonnet.swing;

import gov.ismonnet.bootstrap.Bootstrap;
import gov.ismonnet.lifecycle.LifeCycle;
import gov.ismonnet.lifecycle.LifeCycleService;
import gov.ismonnet.util.SneakyThrow;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class SwingWindow extends JFrame implements LifeCycle {

    private final LifeCycleService lifeCycleService;

    private JPanel currentScreen;

    private final AtomicBoolean disposing = new AtomicBoolean(false);

    @Inject SwingWindow(@Bootstrap LifeCycleService lifeCycleService) {
        this.lifeCycleService = lifeCycleService;

        setTitle("SwingGame");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setSize(1200, 800);
        setMinimumSize(new Dimension(600, 400));
        setResizable(true);
        setLocationRelativeTo(null);

        lifeCycleService.beforeStop(() -> setVisible(false));
        lifeCycleService.register(this);
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
        disposing.set(true);
        dispose();
    }

    @Override
    public void dispose() {
        if(!disposing.getAndSet(true))
            SneakyThrow.runUnchecked(lifeCycleService::stop);
        super.dispose();
        disposing.set(false);
    }

    public void setScreen(JPanel panel) {

        if(panel != currentScreen) {
            if(currentScreen != null)
                remove(currentScreen);

            currentScreen = panel;
            add(panel);
            setContentPane(panel);
            setVisible(true);
        }

        revalidate();
        repaint();
    }
}
