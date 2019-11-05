package gov.ismonnet.client.renderer.swing;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import gov.ismonnet.client.entity.Entity;
import gov.ismonnet.client.renderer.RenderContext;
import gov.ismonnet.client.renderer.RenderService;
import gov.ismonnet.client.renderer.RenderServiceFactory;
import gov.ismonnet.client.renderer.Renderer;
import gov.ismonnet.client.rink.Rink;
import gov.ismonnet.client.util.ScaledResolution;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@AutoFactory(implementing = RenderServiceFactory.class)
public class SwingRenderService extends JPanel implements RenderService {

    private final JFrame frame;
    private ScaledResolution scaledResolution;

    private final Rink rink;

    private final Map<Class, Renderer> renderers;
    private final Renderer<RenderContext, Object> fallbackRenderer;
    private final Renderer<SwingRenderContext, Entity> collisionRenderer;

    private final Runnable ticksHandler;

    @SuppressWarnings("unchecked")
    @Inject SwingRenderService(@Provided Rink rink,
                               @Provided Map<Class<?>, Renderer> renderers,
                               @Provided Renderer<RenderContext, Object> fallbackRenderer,
                               @Provided Renderer collisionRenderer,
                               Runnable ticksHandler) {
        this.rink = rink;

        this.renderers = Collections.unmodifiableMap(new HashMap<>(renderers));
        this.fallbackRenderer = fallbackRenderer;
        this.collisionRenderer = collisionRenderer;

        this.ticksHandler = ticksHandler;

        // Swing swong

        this.frame = new JFrame("SwingGame") {
            @Override
            public void dispose() {
            }
        };
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frame.setSize((int) Math.ceil(rink.getWidth()), (int) Math.ceil(rink.getHeight()));
        this.frame.setResizable(true);
        this.frame.setLocationRelativeTo(null);
        this.frame.setContentPane(this);

        this.scaledResolution = new ScaledResolution(getWidth(), getHeight(), rink.getWidth(), rink.getHeight());
        addComponentListener(new ResizeHandler());

        this.frame.setVisible(true);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void paintComponent(Graphics g) {
        if(!(g instanceof Graphics2D))
            throw new AssertionError("Swing did not create a 2d graphics component :O");

        ticksHandler.run();

        final SwingRenderContext ctx = new SwingRenderContext((Graphics2D) g);

        ctx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        ctx.setBackground(new Color(33, 33, 33));
        ctx.clearRect(0, 0, getWidth(), getHeight());

        setupScaling(ctx, true);

        renderers.getOrDefault(Rink.class, fallbackRenderer).render(ctx, rink);
        rink.getEntities().forEach(entity -> {
            renderers.getOrDefault(entity.getClass(), fallbackRenderer).render(ctx, entity);
            collisionRenderer.render(ctx, entity);
        });

        setupScaling(ctx, false);

        repaint();
    }

    private void setupScaling(SwingRenderContext ctx, boolean enable) {
        if (enable) {
            ctx.translate(scaledResolution.getWidthDifference() / 2, scaledResolution.getHeightDifference() / 2);
            ctx.scale(scaledResolution.getWidthScaleFactor(), scaledResolution.getHeightScaleFactor());
        } else {
            ctx.scale(1 / scaledResolution.getWidthScaleFactor(), 1 / scaledResolution.getHeightScaleFactor());
            ctx.translate(-scaledResolution.getWidthDifference() / 2, -scaledResolution.getHeightDifference() / 2);
        }
    }

    private class ResizeHandler extends ComponentAdapter {
        public void componentResized(ComponentEvent e) {
            scaledResolution = new ScaledResolution(getWidth(), getHeight(), rink.getWidth(), rink.getHeight());
        }
    }
}
