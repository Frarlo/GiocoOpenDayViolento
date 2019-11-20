package gov.ismonnet.game.renderer.swing.screen;

import gov.ismonnet.game.renderer.RenderService;
import gov.ismonnet.game.renderer.swing.SwingRenderService;
import gov.ismonnet.game.renderer.swing.SwingScreen;
import gov.ismonnet.util.ScaledResolution;
import gov.ismonnet.swing.SwingWindow;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

abstract class BaseScreen implements SwingScreen, KeyListener, FocusListener {

    protected final SwingWindow window;
    protected final SwingRenderService renderService;

    BaseScreen(SwingWindow window,
               SwingRenderService renderService) {
        this.window = window;
        this.renderService = renderService;
    }

    @Override
    public void init(ScaledResolution scaledResolution) {
        window.addFocusListener(this);
        window.addKeyListener(this);
    }

    @Override
    public void destroy() {
        window.removeFocusListener(this);
        window.removeKeyListener(this);
    }

    @Override
    public void focusGained(FocusEvent e) {
    }

    @Override
    public void focusLost(FocusEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public RenderService.Side getSide() {
        return renderService.getSide();
    }

    public ScaledResolution getScaledResolution() {
        return renderService.getScaledResolution();
    }

    public float getWidth() {
        return renderService.getScaledResolution().getWidth();
    }

    public float getHeight() {
        return renderService.getScaledResolution().getHeight();
    }
}
