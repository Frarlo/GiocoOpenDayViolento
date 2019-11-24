package gov.ismonnet.game.renderer.swing.screen;

import gov.ismonnet.game.renderer.RenderService;
import gov.ismonnet.game.renderer.swing.SwingRenderService;
import gov.ismonnet.game.renderer.swing.SwingScreen;
import gov.ismonnet.swing.SwingWindow;
import gov.ismonnet.util.ScaledResolution;

import java.awt.event.*;

abstract class BaseScreen implements SwingScreen,
        KeyListener, FocusListener,
        MouseListener, MouseMotionListener, MouseWheelListener {

    protected final SwingWindow window;
    protected final SwingRenderService renderService;

    BaseScreen(SwingWindow window,
               SwingRenderService renderService) {
        this.window = window;
        this.renderService = renderService;
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

    // Event listeners
    //
    // Standard behavior which cannot be overridden can be added in
    // the final methods, while the on-prefixed ones are the overridable ones

    // Creation

    @Override
    public final void init(ScaledResolution scaledResolution) {
        window.addFocusListener(this);
        window.addKeyListener(this);
        renderService.addMouseListener(this);
        renderService.addMouseMotionListener(this);
        renderService.addMouseWheelListener(this);

        this.onInit(scaledResolution);
    }

    protected void onInit(ScaledResolution scaledResolution) {
    }

    @Override
    public final void destroy() {
        window.removeFocusListener(this);
        window.removeKeyListener(this);
        renderService.removeMouseListener(this);
        renderService.removeMouseMotionListener(this);
        renderService.removeMouseWheelListener(this);

        this.onDestroy();
    }

    protected void onDestroy() {
    }

    // Focus

    @Override
    public final void focusGained(FocusEvent e) {
        onFocusGained(e);
    }

    protected void onFocusGained(FocusEvent e) {
    }

    @Override
    public final void focusLost(FocusEvent e) {
        onFocusLost(e);
    }

    protected void onFocusLost(FocusEvent e) {
    }

    // key

    @Override
    public final void keyTyped(KeyEvent e) {
        onKeyTyped(e);
    }

    protected void onKeyTyped(KeyEvent e) {
    }

    @Override
    public final void keyPressed(KeyEvent e) {
        onKeyPressed(e);
    }

    protected void onKeyPressed(KeyEvent e) {
    }

    @Override
    public final void keyReleased(KeyEvent e) {
        onKeyReleased(e);
    }

    protected void onKeyReleased(KeyEvent e) {
    }

    // Mouse

    @Override
    public final void mouseClicked(MouseEvent e) {
        onMouseClicked(e);
    }

    protected void onMouseClicked(MouseEvent e) {
    }

    @Override
    public final void mousePressed(MouseEvent e) {
        onMousePressed(e);
    }

    protected void onMousePressed(MouseEvent e) {
    }

    @Override
    public final void mouseReleased(MouseEvent e) {
        onMouseReleased(e);
    }

    protected void onMouseReleased(MouseEvent e) {
    }

    @Override
    public final void mouseEntered(MouseEvent e) {
        onMouseEntered(e);
    }

    protected void onMouseEntered(MouseEvent e) {
    }

    @Override
    public final void mouseExited(MouseEvent e) {
        onMouseExited(e);
    }

    protected void onMouseExited(MouseEvent e) {
    }

    // Mouse Motion

    @Override
    public final void mouseDragged(MouseEvent e) {
        onMouseDragged(e);
    }

    protected void onMouseDragged(MouseEvent e) {
    }

    @Override
    public final void mouseMoved(MouseEvent e) {
        onMouseMoved(e);
    }

    protected void onMouseMoved(MouseEvent e) {
    }

    // Mouse Wheel

    @Override
    public final void mouseWheelMoved(MouseWheelEvent e) {
        onMouseWheelMoved(e);
    }

    protected void onMouseWheelMoved(MouseWheelEvent e) {
    }
}
