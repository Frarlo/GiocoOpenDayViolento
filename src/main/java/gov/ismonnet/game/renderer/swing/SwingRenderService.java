package gov.ismonnet.game.renderer.swing;

import gov.ismonnet.game.physics.PhysicsService;
import gov.ismonnet.game.physics.entity.Entity;
import gov.ismonnet.game.physics.table.Table;
import gov.ismonnet.game.renderer.RenderContext;
import gov.ismonnet.game.renderer.RenderService;
import gov.ismonnet.game.renderer.Renderer;
import gov.ismonnet.game.util.ScaledResolution;
import gov.ismonnet.lifecycle.LifeCycle;
import gov.ismonnet.lifecycle.LifeCycleService;
import gov.ismonnet.swing.BackgroundColor;
import gov.ismonnet.swing.SwingWindow;
import gov.ismonnet.util.SneakyThrow;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SwingRenderService extends JPanel implements RenderService, LifeCycle {

    private Side side;

    private final PhysicsService physicsService;
    private final Table table;

    private final SwingWindow window;
    private final Color backgroundColor;
    private ScaledResolution scaledResolution;

    private final Map<Class, Renderer> renderers;
    private final Renderer<RenderContext, Object> fallbackRenderer;
    private final Renderer<SwingRenderContext, Entity> axisAlignedBBsRenderer;

    private final Robot robot;
    private final FocusListener focusListener;

    @SuppressWarnings("unchecked")
    @Inject SwingRenderService(SwingWindow window,
                               @BackgroundColor Color backgroundColor,
                               Side side,
                               PhysicsService physicsService,
                               Table table,
                               Map<Class<?>, Renderer> renderers,
                               Renderer<RenderContext, Object> fallbackRenderer,
                               Renderer axisAlignedBBsRenderer,
                               LifeCycleService lifeCycleService) {
        this.window = window;
        this.backgroundColor = backgroundColor;
        this.side = side;

        this.physicsService = physicsService;
        this.table = table;

        this.renderers = Collections.unmodifiableMap(new HashMap<>(renderers));
        this.fallbackRenderer = fallbackRenderer;
        this.axisAlignedBBsRenderer = axisAlignedBBsRenderer;

        this.scaledResolution = new ScaledResolution(getWidth(), getHeight(), table.getWidth(), table.getHeight());
        addComponentListener(new ResizeHandler());

        this.robot = SneakyThrow.callUnchecked(Robot::new);
        addMouseMotionListener(new MouseHandler());
        window.addFocusListener(focusListener = new FocusHandler());
        // Hide cursor for this panel
        // Thanks to https://stackoverflow.com/a/10687248 and https://stackoverflow.com/a/1984117
        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
                new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB),
                new Point(),
                "blank cursor"));

        lifeCycleService.register(this);
    }

    @Override
    public void start() {
        window.setScreen(this);
    }

    @Override
    public void stop() {
        window.removeFocusListener(focusListener);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(!(g instanceof Graphics2D))
            throw new AssertionError("Swing did not create a 2d graphics component :O");

        physicsService.handleTicks();

        final SwingRenderContext ctx = new SwingRenderContext((Graphics2D) g);

        ctx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        ctx.setBackground(backgroundColor);
        ctx.clearRect(0, 0, getWidth(), getHeight());

        setupScaling(ctx, true);

        renderers.getOrDefault(Table.class, fallbackRenderer).render(ctx, table);
        physicsService.getEntities().forEach(entity -> {
            renderers.getOrDefault(entity.getClass(), fallbackRenderer).render(ctx, entity);
            axisAlignedBBsRenderer.render(ctx, entity);
        });

        setupScaling(ctx, false);

        repaint();
    }

    private void setupScaling(SwingRenderContext ctx, boolean enable) {
        if (enable) {
            if(side == Side.RIGHT) {
                ctx.scale(-1, 1);
                ctx.translate(-getWidth(), 0);
            }

            ctx.translate(scaledResolution.getWidthDifference(), scaledResolution.getHeightDifference() / 2);
            ctx.scale(scaledResolution.getWidthScaleFactor(), scaledResolution.getHeightScaleFactor());
        } else {
            ctx.scale(1 / scaledResolution.getWidthScaleFactor(), 1 / scaledResolution.getHeightScaleFactor());
            ctx.translate(-scaledResolution.getWidthDifference(), -scaledResolution.getHeightDifference() / 2);

            if(side == Side.RIGHT) {
                ctx.scale(-1, 1);
                ctx.translate(getWidth(), 0);
            }
        }
    }

    private void onMouseMoved(MouseEvent e) {
        // Just if this window is focused
        if(!window.isActive())
            return;
        // Thanks to https://stackoverflow.com/a/32159962
        // Moved by Robot, don't care
        if(e.getX() == getWidth() / 2 && e.getY() == getHeight() / 2)
            return;
        // Move the mouse back to the center
        centerCursor();
        // Register the actual movement
        final int moveX = e.getX() - getWidth() / 2;
        final int moveY = e.getY() - getHeight() / 2;
        System.out.println("moved: " + moveX + " " + moveY);
    }

    private void centerCursor() {
        // Move the mouse back to the center
        final Point p = getLocationOnScreen();
        robot.mouseMove( (int) p.getX() + getWidth() / 2, (int) p.getY() + getHeight() / 2);
    }

    private class MouseHandler extends MouseAdapter {
        @Override
        public void mouseDragged(MouseEvent e) {
            onMouseMoved(e);
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            onMouseMoved(e);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            centerCursor();
        }
    }

    public class FocusHandler extends FocusAdapter {
        @Override
        public void focusGained(FocusEvent e) {
            centerCursor();
        }
    }

    private class ResizeHandler extends ComponentAdapter {
        public void componentResized(ComponentEvent e) {
            scaledResolution = new ScaledResolution(getWidth(), getHeight(), table.getWidth(), table.getHeight());
        }
    }

    @Override
    public Side getSide() {
        return side;
    }

    @Override
    public void switchSide() {
        side = (side == Side.LEFT) ? Side.RIGHT : Side.LEFT;
    }
}
