package gov.ismonnet.swing;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import gov.ismonnet.bootstrap.Bootstrap;
import gov.ismonnet.lifecycle.LifeCycle;
import gov.ismonnet.lifecycle.LifeCycleService;
import gov.ismonnet.util.SneakyThrow;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicBoolean;

@AutoFactory
public class SwingWindow extends JFrame implements LifeCycle {

    private final LifeCycleService lifeCycleService;

    private JPanel currentScreen;

    private final MouseGrabHandler mouseGrabHandler;
    private final Cursor blankCursor;

    private final AtomicBoolean disposing = new AtomicBoolean(false);

    @Inject SwingWindow(@Provided @Bootstrap LifeCycleService lifeCycleService) {
        this.lifeCycleService = lifeCycleService;

        setTitle("SwingGame");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setSize(1200, 800);
        setMinimumSize(new Dimension(600, 400));
        setResizable(true);
        setLocationRelativeTo(null);

        mouseGrabHandler = new MouseGrabHandler(this);
        Toolkit.getDefaultToolkit().addAWTEventListener(mouseGrabHandler,
                AWTEvent.FOCUS_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
        // Hide cursor for this panel
        // Thanks to https://stackoverflow.com/a/10687248 and https://stackoverflow.com/a/1984117
        blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB),
                new Point(),
                "blank cursor");

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

    public void setScreen(JPanel newScreen) {
        if(newScreen == currentScreen)
            return;

        final JPanel oldScreen = currentScreen;
        currentScreen = newScreen;

        EventQueue.invokeLater(() -> {
            if(oldScreen != null)
                remove(oldScreen);

            add(newScreen);
            setContentPane(newScreen);
            setVisible(true);
        });
    }

    public JPanel getCurrentScreen() {
        return currentScreen;
    }


    public void setMouseGrabbed(boolean isMouseGrabbed) {
        mouseGrabHandler.isMouseGrabbed = isMouseGrabbed;
        setCursor(isMouseGrabbed ? blankCursor : Cursor.getDefaultCursor());
    }

    public boolean isMouseGrabbed() {
        return mouseGrabHandler.isMouseGrabbed;
    }

    static class MouseGrabHandler implements AWTEventListener {

        private final Window window;
        private final Robot robot;

        private boolean isMouseGrabbed;

        MouseGrabHandler(Window window) {
            this.window = window;
            this.robot = SneakyThrow.callUnchecked(Robot::new);
        }

        @Override
        public void eventDispatched(AWTEvent event) {
            if(event.getID() == MouseEvent.MOUSE_DRAGGED)
                mouseDragged((MouseEvent) event);
            else if(event.getID() == MouseEvent.MOUSE_MOVED)
                mouseMoved((MouseEvent) event);
            else if(event.getID() == FocusEvent.FOCUS_GAINED)
                focusGained((FocusEvent) event);
        }

        private void mouseDragged(MouseEvent e) {
            onMouseMoved(e);
        }

        private void mouseMoved(MouseEvent e) {
            onMouseMoved(e);
        }

        private void focusGained(FocusEvent e) {
            if(isMouseGrabbed)
                centerCursor();
        }

        private void onMouseMoved(MouseEvent e) {
            // Just if this window is focused
            if(!isMouseGrabbed || !window.isFocused())
                return;
            // Thanks to https://stackoverflow.com/a/32159962
            // Moved by Robot, don't care
            if(e.getX() == window.getWidth() / 2 && e.getY() == window.getHeight() / 2)
                return;
            // Move the mouse back to the center
            centerCursor();
            // Register the actual movement
//            final int moveX = e.getX() - window.getWidth() / 2;
//            final int moveY = e.getY() - window.getHeight() / 2;
//            System.out.println("moved: " + moveX + " " + moveY);
        }

        private void centerCursor() {
            // Move the mouse back to the center
            final Point p = window.getLocationOnScreen();
            robot.mouseMove( (int) p.getX() + window.getWidth() / 2, (int) p.getY() + window.getHeight() / 2);
        }
    }
}
