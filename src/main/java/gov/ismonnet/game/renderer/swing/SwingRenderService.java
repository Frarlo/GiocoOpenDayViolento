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
import gov.ismonnet.swing.SwingWindow;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SwingRenderService extends JPanel implements RenderService, LifeCycle {

    private static final Color BACKGROUND_COLOR = new Color(33, 33, 33);

    private Side side;

    private final PhysicsService physicsService;
    private final Table table;

    private final SwingWindow window;
    private final LifeCycleService lifeCycleService;
    private ScaledResolution scaledResolution;

    private final Map<Class, Renderer> renderers;
    private final Renderer<RenderContext, Object> fallbackRenderer;
    private final Renderer<SwingRenderContext, Entity> axisAlignedBBsRenderer;

    @SuppressWarnings("unchecked")
    @Inject SwingRenderService(SwingWindow window,
                               Side side,
                               PhysicsService physicsService,
                               Table table,
                               Map<Class<?>, Renderer> renderers,
                               Renderer<RenderContext, Object> fallbackRenderer,
                               Renderer axisAlignedBBsRenderer,
                               LifeCycleService lifeCycleService) {
        this.side = side;

        this.physicsService = physicsService;
        this.table = table;

        this.window = window;
        this.lifeCycleService = lifeCycleService;

        this.renderers = Collections.unmodifiableMap(new HashMap<>(renderers));
        this.fallbackRenderer = fallbackRenderer;
        this.axisAlignedBBsRenderer = axisAlignedBBsRenderer;

        this.scaledResolution = new ScaledResolution(getWidth(), getHeight(), table.getWidth(), table.getHeight());
        addComponentListener(new ResizeHandler());

        lifeCycleService.register(this);
    }

    @Override
    public void start() {
        window.setScreen(this);
    }

    @Override
    public void stop() {
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void paintComponent(Graphics g) {
        if(!(g instanceof Graphics2D))
            throw new AssertionError("Swing did not create a 2d graphics component :O");

        physicsService.handleTicks();

        final SwingRenderContext ctx = new SwingRenderContext((Graphics2D) g);

        ctx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        ctx.setBackground(BACKGROUND_COLOR);
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
